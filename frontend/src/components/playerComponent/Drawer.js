import React, {useEffect, useState} from "react";
import CommunityPart from "./CommunityPart";
import Stomp from "stompjs";
import SockJS from "sockjs-client";
import Cookies from "universal-cookie";
import Canvas from "./Canvas"
import "../../styles/Player.css"
import {BACK_ADDRESS} from "../../constants";


const Drawer = () => {

    const [color, setColor] = useState("black");
    const [width, setWidth] = useState(5);
    const [drawMessage, setDrawMessage] = useState("");
    const [chatMessage, setChatMessage] = useState("");
    const [head, setHead] = useState(true);
    const [socket, setSocket] = useState( new SockJS(BACK_ADDRESS + '/game'));
    const [stompClient, setStompClient] = useState(Stomp.over(socket));
    const cookies = new Cookies();
    const [user, setUser] = useState("")
    const [master, setMaster] = useState(NaN)
    const [chooseWords, setChooseWords] = useState([]);
    const [timeLeft, setTimeLeft] = useState(0);

    useEffect(() => {
        console.log("use effect")
        stompClient.connect({}, function (frame) {
            let room_id = cookies.get('Room_id');
            console.log("mount Drawer/ room id: " + room_id);
            stompClient.subscribe('/topic/session/' + room_id, greed);
            stompClient.subscribe('/user/queue/session', greed);
            stompClient.send('/app/join/' + room_id, {});
        });
        //setChooseWords([...chooseWords, "стул", "окно", "стол"]);
    }, [])


    function greed(message) {
        let json = JSON.parse(message.body);
        switch (json.type) {
            case "ANSWERS_CHOICE":
                choseAnswers(json.answers);
                break;
            case "GREETING":
                greeting(json);
                break;
            case "IMAGE_UPDATE":
                setDrawMessage(json);
                break;
            case "CHAT_MESSAGE":
                setChatMessage(json);
                break;
            case "NEW_MASTER":
                newMaster(json);
                break;
            case "TIME_LEFT":
                if (json.time >= 0)
                    setTimeLeft(json.time)
                else
                    setTimeLeft(0);
                break;                
        }
        /*
        if (json.type === "ANSWERS_CHOICE"){
            setChooseWords(json.answers);
        }
        if (json.type === "GREETING") {
            greeting(json);
        }
        if (json.type === "IMAGE_UPDATE") {
            setDrawMessage(json);
        } else if
        (json.type === "CHAT_MESSAGE")
            setChatMessage(json);*/
    }


    function newMaster(message){
        setMaster(message.masterId);
        setHead(user === message.masterId);
    }
    function choseAnswers(ans) {
        setChooseWords([]);
        setChooseWords(ans);
        console.log(chooseWords)
        setHead(true);
    }

    function greeting(message) {
        setUser(message.user);
        setMaster(message.master);
        setHead(message.user === message.master)
    }


    const sendDrawMessage = (json) => {
        if (json !== "")
            try {
                stompClient.send('/app/session/draw', {}, JSON.stringify(json));
            } catch (e) {
                console.log(e)
                console.log("Error!");
            }
    }

    const sendWord = (word) => {
        stompClient.send('/app/session/choose', {}, JSON.stringify({"choice": word}));
    }

    const sendChatMessage = (input) => {
        let json = {"message" : input}
        if (json !== "")
            try {
                stompClient.send('/app/session/chat', {}, JSON.stringify(json));//fixx!!!
            } catch (e) {
                console.log("Send mes Error!");
            }
        console.log(json);
    }


    const changeColor = (newColor) => {
        setColor(newColor);
    }
    const changeWidth = (width) => {
        setWidth(width);
    }

    const chC = (e) => {
        changeColor(e.target.value);
    }

    const chW = (e) => {
        changeWidth(e.target.value);
    }


    return (
        <div className="parent-container">
            <div className="draw-board">
                <Canvas
                    CanWidth={1200}
                    CanHeight={720}
                    color={color}
                    width={width}
                    head={head}
                    message={drawMessage}
                    callBackMessage={sendDrawMessage}
                />
            </div>
            <div className="communication-part">
                <CommunityPart callBackChat={sendChatMessage} serverMessage={chatMessage} chooseWord={chooseWords} head={head} chosenWord={sendWord}/>
            </div>
            <div className="drawing-things">
                <img className={"colorImage"} src={"gameField/yellow.png"} onClick={() => {
                    changeColor("yellow")
                }} alt={"no"}/>
                <img className={"colorImage"} src={"gameField/red.png"} onClick={() => {
                    changeColor("red")
                }} alt={"no"}/>
                <img className={"colorImage"} src={"gameField/black.png"} onClick={() => {
                    changeColor("black")
                }} alt={"no"}/>
                <img className={"colorImage"} src={"gameField/green.png"} onClick={() => {
                    changeColor("green")
                }} alt={"no"}/>
                <img className={"colorImage"} src={"gameField/blue.png"} onClick={() => {
                    changeColor("blue")
                }} alt={"no"}/>
                <img className={"colorImage"} src={"gameField/brown.png"} onClick={() => {
                    changeColor("brown")
                }} alt={"no"}/>
                <img className={"colorImage"} src={"gameField/rubber.png"} onClick={() => {
                    changeColor("white");
                }} alt={"no"}/>
                <input type="color" className="color-switch" onChange={chC}></input>
                <input type="range" min={1} max={20} onChange={chW}></input>
            </div>
        </div>
    )
}
export default Drawer;