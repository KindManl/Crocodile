import '../styles/App.css';
import {Route, BrowserRouter, Routes} from "react-router-dom";
import RoomStore from "./RoomStore";
import About from "./About";
import Home from "./Home";
import Room from "./Room";
import Authorization from "./Authorization";
import Registration  from "./Registration";
import Recovery from "./Recovery"
import RecoveryResult from "./RecoveryResult";
import React from "react";

const App = () => {


    return(
       <BrowserRouter>
            <div>
                <Routes>
                    <Route path={'/RoomStore'} element= <RoomStore/> />
                    <Route  path='/About' element= <About/> />
                    <Route  path='/' element= <Home/> />
                    <Route path={'/Room'} element= <Room/> />
                    <Route path={'/Authorization'} element = <Authorization/> />
                    <Route path={'/Registration'} element=<Registration/> />
                    <Route path={'/Recovery'} element=<Recovery/> />
                    <Route path={'/RecoveryResult'} element=<RecoveryResult/> />
                </Routes>
            </div>
        </BrowserRouter>

    )
}

export default App;