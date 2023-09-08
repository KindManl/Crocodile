import React from "react";
import '../../styles/RoomStyle.css';
import Cookies from 'universal-cookie';
import {BACK_ADDRESS} from "../../constants";

const RoomCreateComponent = () => {

    const[num, setNum] = React.useState('');
    const [nam, setName] = React.useState('');
    const [pass, setPass] = React.useState('');
    const cookies = new Cookies();



    const newMessage = (e) => {
        let a = e.target.value;
        setNum(a);
        if (num === ''){
            document.getElementById('CountPlayers').style.color = "black";
        }
    }

    const newName = (e) => {
        let a = e.target.value;
        setName(a);

    }

    const newPass = (e) => {
        let a = e.target.value;
        setPass(a);

    }

    const checkMessage = () => {
        if (!isNaN(parseFloat(num)) && !isNaN(num - 0) && (nam.length <= 36) ){
            document.getElementById('CountPlayers').style.color = "green";
            fetch(BACK_ADDRESS + '/lobby/create ', {
                method: 'POST',
                headers:  {
                    'Accept': 'application/json',
                    'Content-Type' : 'application/json',
                    'Authorization': 'Basic ' + cookies.get('B64'),
                },
                body: JSON.stringify({
                    name: nam,
                    password: pass,
                    hidden: false,
                    capacity: num,

                })
            }).then(response => response.json())
                .then(result => {
                cookies.set('Room_id', result.id, {path: '/'});
                fetch(BACK_ADDRESS + '/lobby/join ', {
                        method: 'POST',
                        headers:  {
                            'Accept': 'application/json',
                            'Content-Type' : 'application/json',
                            'Authorization': 'Basic ' + cookies.get('B64'),
                        },
                        body: JSON.stringify({
                            id: cookies.get('Room_id'),
                            password: pass,

                        })
                    }).then((res) => {
                        if (res.ok) {
                            window.location.assign('Room');
                        }          })
            })
        }
        else {
            if(nam.length > 36){
                document.getElementById('Nameroom').style.color = "red";
            }
            else{
                document.getElementById('Nameroom').style.color = "black";
            }
            if (isNaN(parseFloat(num)) || isNaN(num - 0)){
            document.getElementById('CountPlayers').style.color = "red";}
            else {
                document.getElementById('CountPlayers').style.color = "black";}
            }
        }


    return (

        <div>
            <button className="game-creation-button" style={{fontSize: 36, fontWeight: 700}} onClick={checkMessage}>
                Создать комнату
            </button>
            <div className="game-creation-field">
                <label className="lable-text1"> Название:</label>
                <label className="lable-text2"> Количество мест:</label>
                <label className="lable-text3"> Пароль:</label>
                <label className="lable-text4"> Скрытая:</label>
                <input className="number-players" type="text" id="CountPlayers" onChange={newMessage}/>
                <input className="name" type="text" id="Nameroom" onChange={newName}/>
                <input className="passw" type="text" id="CountPlayers" onChange={newPass}/>
                <input type="checkbox" className="checkbox-room" />

            </div>
        </div>
    )
}

export default RoomCreateComponent;