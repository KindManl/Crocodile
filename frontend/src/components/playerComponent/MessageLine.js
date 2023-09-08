import React, {useState} from "react";

export default function MessageLine({name, text, liked, disliked}) {
    const [like, setLike] = useState(liked);
    const [dis, setDis] = useState(disliked);

    return(
        <div className={"messageLine"}>
            <img className={"likeImage"} src={like === false ? "gameField/likeUp.png" : "gameField/likeUpActive.png"} onClick={()=>{setLike(!like); setDis(false)}} alt={"like"}/>
            <img className={"likeImage"} src={dis === false ? "gameField/likeDown.png" : "gameField/likeDownActive.png"} onClick={()=>{setDis(!dis); setLike(false)}} alt={"dis"}/>
            <ul className={"textOfMessage"}>{name}: {text}</ul>
        </div>
        )

}