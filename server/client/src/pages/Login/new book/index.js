import React, {useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import { FiArrowLeft} from 'react-icons/fi';

import api from '../../../services/api';

import './style.css';

import Logo from '../../../asset/logo.svg';

export default function NewBook() {
    const[id, setId] = useState(null);
    const[title, setTitle] = useState('');
    const[author, setAuthor] = useState('');
    const[price, setPrice] = useState('');
    const[launch_date, setLaunch_date] = useState('');

    const username = localStorage.getItem('username');
    const accessToken = localStorage.getItem('accessToken');
    console.log("token: ", accessToken);

    const navigate = useNavigate();
    
    async function createNewBook(e) { 
        e.preventDefault(); 
        
        const data = {
            title, author, price, launch_date, 
        }

        try {
            const response = await api.post('/books/createBook', data, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                }
            });
            navigate('/books')
        } catch(err) {
            alert('error while recording book. Try again.', err);
        };
    }

    return (
        <div className='new-book-container'>
            <div className='content'>
                <section className='form'>
                    <img src={Logo} alt="Erudio"></img>
                    <h1>Add New Book</h1>
                    <p>Enter the book information and click on 'Add'!</p>
                    <Link className='back-link' to='/books'>
                        <FiArrowLeft size={16} color='#251FC5'></FiArrowLeft>
                        Home
                    </Link>
                </section>
                <form onSubmit={createNewBook}>
                    <input
                    placeholder='Title' value={title}
                    onChange={e => setTitle(e.target.value)}></input>
                    <input
                    placeholder='Author' value={author}
                    onChange={e => setAuthor(e.target.value)}></input>
                    <input
                    type='date' value={launch_date}
                    onChange={e => setLaunch_date(e.target.value)}></input>
                    <input
                    placeholder='Price' value={price}
                    onChange={e => setPrice(e.target.value)} ></input>

                    <button className='button' type='submit'>Add</button>
                </form>
            </div>
        </div>
    );
}
