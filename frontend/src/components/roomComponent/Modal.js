import React, {useState} from "react";
import '../../styles/RoomStyle.css';
import Cookies from 'universal-cookie';
import {decode as base64_decode, encode as base64_encode} from 'base-64';
import {BACK_ADDRESS} from "../../constants";
const Modal = props => {
    const [password, setpassword] = useState('');

    const newPass = (e) => {
        let a = e.target.value;
        setpassword(a);

    }

    if (!props.show){
        return null
    }

    const cookies = new Cookies();
    return(
        <div className="modal">
            <h4 className="modal-title"> Введите пароль</h4>
            <input className="modal-input" onChange={newPass}/>
            <button onClick={props.onClose} className="modal-close"> Отмена</button>
            <button onClick={() => fetch(BACK_ADDRESS + '/lobby/join ', {
                method: 'POST',
                headers:  {
                    'Accept': 'application/json',
                    'Content-Type' : 'application/json',
                    'Authorization': 'Basic ' + cookies.get('B64'),
                },
                body: JSON.stringify({
                    id: cookies.get('Room_id'),
                    password: password,

                })
            }).then((res) => {
                if (res.ok) {
                    window.location.assign('Room');
                }
            }) } className="modal-in"> Вход</button>
        </div>
    )
}

export default Modal