import React, {Component} from 'react';

import '../styles/Recovery.css';
import {BACK_ADDRESS} from "../constants";
class Recovery extends Component {

    constructor(props) {
        super(props);

        this.state = {
            email: '',

        };

        this.handleEmailChange = this.handleEmailChange.bind(this);
    }

    handleEmailChange(e) {
        this.setState({ email: e.target.value });
    }


    onclick () {
        fetch(BACK_ADDRESS + '/auth/password-reset', {
            method: 'POST',
            headers:  {
                'Accept': 'application/json',
                'Content-Type' : 'application/json',
            },
            body: JSON.stringify({
                email: this.state.email,

            })
        }).then((res) => {window.location.assign('/RecoveryResult');
        });

    }

    render() {
        return (
                <div className = "recovery">
                    <label className="info"> Введите почту, на которую был зарегистрирован аккаунт:</label>
                    <input className="recovery-post" type="text" onChange={this.handleEmailChange}/>
                    <button className="recovery-button" onClick={(e) => this.onclick(e)}> Восстановить пароль </button>
                    <a className="authorization-from-recovery" href='/Authorization' > Вспомнил пароль</a>
                </div>
        );
    }
}

export default Recovery;