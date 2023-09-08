import React, {useEffect, useState} from "react";
import MessageList from './MessageList'
import WordPicker from './WordPicker'
import "../../styles/Player.css"




const CommunityPart = ({callBackChat, serverMessage, chooseWord, chosenWord, head}) => {
    const [Messages, setMessages] = useState([])
    const[message, setMessage] = useState('');
    const[activeIndex, setActiveIndex] = useState(0);
    const[timeToChoose, setTimeToChoose] = useState(true);


    useEffect(()=>{
        console.log("mount Chat")
            try {
                addMessageFromServer(serverMessage);
            } catch (e) {
                console.log("Chat Error!");
            }
    }, [serverMessage])

    useEffect(()=>{
        setTimeToChoose(true);
    }, [chooseWord])


    const addMessageFromServer = (body) => {
        if (body.message !== undefined) {
            setMessages([
                ...Messages,
                {//json parse part
                    name: body.username,
                    text: body.message,
                    liked: body.liked,
                    disliked: body.disliked
                }
            ])
            console.log(body.message);
        }
    }

    const addMessage = () => {
        callBackChat(message);
    }


    const newMessage = (e) => {
        let a = e.target.value;
        setMessage(a)
        if (a !== '')
            setActiveIndex(1);
        else
            setActiveIndex(0);
    }

    const sendClick = () => {
        if ((message !== '') && (head === false)) {
            addMessage()//warning на ключи
            setMessage('');
            setActiveIndex(0);
        }
        else
            console.log("nothing");
    }

    const checkButton = (e) => {
        if (e.code === "Enter")
            sendClick();
    }

    const chosen = (word) => {
        setTimeToChoose(false);
        chosenWord(word)
        //send!
    }

    return (
        <div className="chat">
            <div className="text-field">
                <MessageList Messages={Messages}/>
            </div>
            <div className="enter-button">
                <input type="text" placeholder="Что же это а?.." className="chatInput" value={message} onChange={newMessage} onKeyUp={checkButton}></input>
                <img className="send-button" src={activeIndex === 0 ?"gameField/send.png" : "gameField/sendActive.png"} onClick={sendClick} alt={"no"}/>
            </div>
            <div className={timeToChoose === true ? "choose" : "choose false"}>
                <WordPicker  item={chooseWord} callBack={chosen}/>
            </div>
        </div>
    )
}
export default CommunityPart