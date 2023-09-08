import React, {useEffect, useState} from "react";
import '../../styles/RoomStyle.css';
import {useTable} from "react-table";
import {useSortBy} from "react-table";
import Modal from "./Modal"
import Cookies from 'universal-cookie';
import {BACK_ADDRESS} from "../../constants";


const TableComponent = () => {
    const [selected, setSelected] = useState('1');
    const [show, setShow] = useState(false);
   const [data, setdata] = useState([])
    const cookies = new Cookies();

    useEffect(() =>{fetch(BACK_ADDRESS + '/lobby/rooms ', {
            method: 'GET',
            headers:  {
                'Accept': 'application/json',
                'Content-Type' : 'application/json',
                'Authorization': 'Basic ' + cookies.get('B64'),
            },
        }).then(response => response.json()).then((res) => setdata(res.rooms))}, []);


    const columns = React.useMemo(
        () => [
            {
                Header: 'Название',
                accessor: 'name',
            },
            {
                Header: 'Вместимость',
                accessor: 'capacity',
            },
            {
                Header: 'Игроков',
                accessor: 'playersAmount',
            },
            {
                Header: 'Пароль',
                accessor: 'requiresPassword',
            }
        ],
        []
    )

    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        rows,
        prepareRow,
    } = useTable({ columns, data }, useSortBy);


    return (
        <div className="table-component">
            <table {...getTableProps()} style={{ }}>
                <thead> {headerGroups.map(headerGroup => (
                    <tr {...headerGroup.getHeaderGroupProps()}>
                        {headerGroup.headers.map(column => (
                            <th {...column.getHeaderProps(column.getSortByToggleProps())} style={{borderBottom: 'solid 3px #B7F46E', color: 'black', width: 250, height: 30}}>
                             {column.render('Header')}
                            <span>{column.isSorted ? column.isSortedDesc ? '↓' : '↑' : ''}</span>
                            </th>
                        ))}
                    </tr>
                ))}
                </thead>
                <tbody {...getTableBodyProps()}>
                {rows.map(row => {
                    prepareRow(row)
                    return (
                        <tr  {...row.getRowProps()} id = {row.id} >
                            {row.cells.map(cell => {
                                return (
                                    <td
                                        {...cell.getCellProps()}
                                        style={{
                                            padding: '10px',
                                            border: 'solid 1px #d1d1d1',
                                        }}
                                        onClick={() => {
                                            // document.getElementById(selected).style.background = "#FFFFFF";
                                            // document.getElementById(row.id).style.background = "#B7F46E";
                                            setSelected(row.id)}}
                                        onDoubleClick={() => {setShow(true);
                                            // document.getElementById(selected).style.background = "#FFFFFF";
                                            // document.getElementById(row.id).style.background = "#B7F46E";
                                            setSelected(row.id);
                                            console.log('room id:' + row.original.id)
                                            cookies.set('Room_id', row.original.id, {path: '/'})}}
                                    >
                                        {cell.render('Cell')}

                                    </td>
                                )
                            })}
                        </tr>
                    )
                })}
                </tbody>
            </table>
            <Modal onClose={() => setShow(false)} show={show}  />

        </div>
    );
}

export default TableComponent;