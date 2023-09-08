import {useOnDraw} from './CustomHooks';
import React, {useEffect} from "react";


const Canvas = ({CanWidth, CanHeight, color, width, head, message, callBackMessage}) => {


    useEffect(() => {
        console.log("mount Canvas")
        console.log("head:" + head)
        if (head !== true) {
            try {
                let json = message;
                if (json.type === "IMAGE_UPDATE") {
                    let ctx = getCanvasRef();
                    drawLine(json.point, json.prevPoint, ctx, json.color, json.width);
                }
            } catch (e) {
                console.log(message)
                console.log("Canvas Error! " + e);
            }
        }
    }, [message])
    
    useEffect(()=>{
        let ctx = getCanvasRef();
        ctx.clearRect(0,0, CanWidth, CanHeight);
    },[head])


    /*function greed(message){
        let json = JSON.parse(message.body);
        if (json.type === "IMAGE_UPDATE") {
            getCanvasRef();
            let ctx = getCanvasRef();
            drawLine(json.point, json.prevPoint, ctx, json.color, json.width);
        }
    }*/


    const {setCanvasRef, onCanvasMouseDown, getCanvasRef} = useOnDraw(onDraw);


    function onDraw(ctx, point, prevPoint) {
        if (head === true) {
            drawLine(prevPoint, point, ctx, color, width);
            let json = {"prevPoint": prevPoint, "point": point, "color": color, "width": width};
            callBackMessage(json);
            //stompClient.send('/app/session/draw',{}, JSON.stringify(json));
        }
    }

    function length(start, end) {
        return Math.sqrt((start.x - end.x) * (start.x - end.x) + (start.y - end.y) * (start.y - end.y));
    }

    function drawLine(start, end, ctx, color, width) {
        if (end !== null && end !== undefined) {
            start = (start !== null && start !== undefined) ? start : end;
            ctx.beginPath();
            ctx.strokeStyle = color;
            ctx.moveTo(start.x, start.y);
            drawLineCircle(start, end, ctx, color, width);
            ctx.fillStyle = color;
            ctx.fill();
            ctx.stroke();
        }
    }

    function drawLineCircle(start, end, ctx, color, width) {
        const l = length(start, end);
        let i;
        const dx = (end.x - start.x) / l;
        const dy = (end.y - start.y) / l;
        for (i = 0; i < l; ++i) {
            ctx.arc(start.x + i * dx, start.y + i * dy, width / 2, 0, 2 * Math.PI);
            ctx.strokeStyle = color;
        }
    }

    return (
        <div>
            <canvas
                width={CanWidth}
                height={CanHeight}
                onMouseDown={onCanvasMouseDown}
                style={canvasStyle}
                ref={setCanvasRef}
            />
        </div>
    );

}

export default Canvas;

const canvasStyle = {
    border: "1px solid white"
}

