import React, {Component} from 'react';
import '../styles/Authorization.css';
import Cookies from 'universal-cookie';
import {encode as base64_encode} from "base-64";
import {BACK_ADDRESS} from "../constants";



const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1');
const cookies = new Cookies();

class Authorization extends Component {

    constructor(props) {
        super(props);

        this.state = {
            hidden: true,
            password: '',
            login: '',


        };
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.toggleShow = this.toggleShow.bind(this);
        this.checklog = this.checklog.bind(this);
        this.handleLoginChange = this.handleLoginChange.bind(this);


    }


    checklog() {

        fetch(BACK_ADDRESS + '/auth/login?email=' + this.state.login + '&password=' + this.state.password, {
            method: 'POST',
            headers:  {
                'Accept': 'application/json',
                'Content-Type' : 'application/json',
            }
        }).then((res) => {
            if (res.status !== 200){
                document.getElementById('email-exists').style.color = "red";
            }
            else {
                document.getElementById('email-exists').style.color = "#FFFFFF";
                cookies.set('Token', csrfToken, {path: '/'});
                cookies.set('B64', base64_encode(this.state.login + ":" + this.state.password), {path: '/'});
                cookies.set('Login', this.state.login, {path: '/'});
                cookies.set('Password', this.state.password, {path: '/'});
               window.location.assign('/RoomStore');


            }
        })
    }

    handlePasswordChange(e) {
        this.setState({ password: e.target.value });
    }
    handleLoginChange(e) {
        this.setState({ login: e.target.value });
    }

    toggleShow() {
        this.setState({ hidden: !this.state.hidden });
    }

    componentDidMount() {
        if (this.props.password) {
            this.setState({ password: this.props.password });
        }
    }

    render() {
        return (
            <div className="autho">
                <label id="hiy"></label>
                <label className="login-lable"> Почта:</label>
                <label className="email-exists" id = 'email-exists'> Неверно указана почта или пароль</label>
                <input className="login" type="text" onChange={this.handleLoginChange}/>
                <label className="pass-lable"> Пароль:</label>
                <input className="pass"
                       type={this.state.hidden ? 'password' : 'text'}
                       value={this.state.password}
                       onChange={this.handlePasswordChange}></input>
                <button className="eye-buttona" onClick={this.toggleShow}>
                    <img className="eye" alt="no foto" src="eye.png"/>
                </button>
                <button className="in-button" onClick={this.checklog}> Войти </button>
                <a href='/Recovery' className="forgot-password" > Забыли пароль?</a>
                <label className="no-account"> Нет аккаунта?</label>
                <a className="registration" href='/Registration' > зарегистрироваться</a>
            </div>
        );
    }
}

export default Authorization;