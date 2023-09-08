import React, {Component} from 'react';
import '../styles/Registration.css';
import {BACK_ADDRESS} from "../constants";
class Registration extends Component {

    constructor(props) {
        super(props);

        this.state = {
            hiddenfirst: true,
            passwordfirst: '',
            hiddensecond: true,

            passwordsecond: '',
            email: '',
            username: '',
            flag: true
        };

        this.handlePasswordChangeFirst = this.handlePasswordChangeFirst.bind(this);
        this.toggleShowFirst = this.toggleShowFirst.bind(this);
        this.handlePasswordChangeSecond = this.handlePasswordChangeSecond.bind(this);
        this.toggleShowSecond = this.toggleShowSecond.bind(this);
        this.checkreg = this.checkreg.bind(this);
        this.handleNameChange = this.handleNameChange.bind(this);
        this.handleEmailChange = this.handleEmailChange.bind(this);
    }

    checkreg() {

        document.getElementById('email-occupied').style.color = "#FFFFFF";
        document.getElementById('not-match').style.color = "#FFFFFF";
        document.getElementById('password-short').style.color = "#FFFFFF";
        if (this.state.passwordfirst.length < 5){
            document.getElementById('password-short').style.color = "red";
        } else {
            if (this.state.passwordfirst !== this.state.passwordsecond) {
                document.getElementById('not-match').style.color = "red";
            } else {

                fetch(BACK_ADDRESS + '/auth/register', {
                method: 'POST',
                headers:  {
                    'Accept': 'application/json',
                    'Content-Type' : 'application/json',
                },
                body: JSON.stringify({
                    email: this.state.email,
                    username: this.state.username,
                    password: this.state.passwordfirst
                })
                }).then((res) => {
                    if (res.status !== 200){
                        document.getElementById('email-occupied').style.color = "red";

                    }
                    else{

                        window.location.assign('/Authorization');
                    }
                });
               // window.location.assign('/Authorization');


        }
        }
        }

    handlePasswordChangeFirst(e) {
        this.setState({ passwordfirst: e.target.value });
    }

    handleEmailChange(e) {
        this.setState({ email: e.target.value });
    }

    handleNameChange(e) {
        this.setState({ username: e.target.value });
    }

    toggleShowFirst() {
        this.setState({ hiddenfirst: !this.state.hiddenfirst });
    }

    handlePasswordChangeSecond(c) {
        this.setState({ passwordsecond: c.target.value });
    }

    toggleShowSecond() {
        this.setState({ hiddensecond: !this.state.hiddensecond });
    }

    componentDidMount() {
        if (this.props.passwordfirst) {
            this.setState({ passwordfirst: this.props.passwordfirst });
        }
        if (this.props.passwordsecond) {
            this.setState({ passwordsecond: this.props.passwordsecond });
        }
    }

    render() {
        return (
            <div className="regist">
                <label className="nickname-lable"> Никнейм:</label>

                <input className="nickname" type="text" onChange={this.handleNameChange}/>
                <label className="post-lable"> Почта:</label>
                <label className="email-occupied" id = 'email-occupied'>К данной почте уже привязан аккаунт </label>
                <input className="post" type="text" onChange={this.handleEmailChange}/>
                <label className="password-lable"> Пароль:</label>
                <label className="password-short" id = 'password-short'>Пароль слишком короткий - минимум 5 символов</label>
                <input className="password"
                       type={this.state.hiddenfirst ? 'password' : 'text'}
                       value={this.state.passwordfirst}
                       onChange={this.handlePasswordChangeFirst}/>
                <button className="eye-button" onClick={this.toggleShowFirst}>
                    <img className="eye" alt="no foto" src="eye.png"/>
                </button>
                <label className="passworddb-lable"> Повторите пароль:</label>
                <label className="not-match" id = 'not-match'> Пароли несовпадают</label>
                <input className="passworddb" id = 'passworddb'
                       type={this.state.hiddensecond ? 'password' : 'text'}
                       value={this.state.passwordsecond}
                       onChange={this.handlePasswordChangeSecond}/>
                <button className="eyedb-button" onClick={this.toggleShowSecond}>
                    <img className="eye" alt="no foto" src="eye.png"/>
                </button>
                <button className="reg-button" onClick={this.checkreg}> Зарегистрироваться </button>
                <a className="authorization" href='/Authorization' > Уже есть аккаунт</a>
            </div>
        );
    }
}

export default Registration;