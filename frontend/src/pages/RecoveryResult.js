import React, {Component} from 'react';
import '../styles/RecoveryResult.css';
class RecoveryResult extends Component {

    onclick () {

        window.location.assign('/Authorization');
    }

    render() {
        return (
            <div className = "recovery-result">
                <label className="info1"> Если указанная почта есть в системе,то на нее придет письмо с новым паролем</label>
                <button className="recovery-button" onClick={(e) => this.onclick(e)} > Вернуться на главный экран </button>
            </div>
        );
    }
}

export default RecoveryResult;