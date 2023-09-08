import React from "react";
import MessageLine from "./MessageLine"



export default function MessageList ({Messages}) {
    return(
        <div>
            {Messages.map(item => <MessageLine key = {item.id} {...item}/>)}
        </div>
    )
}
