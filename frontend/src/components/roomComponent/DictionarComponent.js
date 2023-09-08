import React, { useState } from "react";
import { MultiSelect } from "react-multi-select-component";

const DictionarComponent = () => {
    const [selected, setSelected] = useState([]);
    
    const options = [
        { label: "Мем", value: "Мем" },
        { label: "Животные", value: "Животные" },
        { label: "Пословицы", value: "Пословицы" },
        { label: "Школа", value: "Школа" },
        { label: "Профессии", value: "Профессии" },
        { label: "Легкие", value: "Легкие"},
        { label: "Сложные", value: "Сложные"},
    ];

    return (
        <div className="dictionaries">
            <MultiSelect style={{fontFamily: "Inter",
                fontStyle: "normal",
                fontWeight: 400,
                fontSize: 24,
                lineHeight: 20}}
                options={options}
                value={selected}
                onChange={setSelected}
                labelledBy="Select"
            />
        </div>
    );
};

export default DictionarComponent;