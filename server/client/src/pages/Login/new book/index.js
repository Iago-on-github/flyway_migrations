import React, {useState, useEffect} from 'react';
import {Link, useNavigate, useParams} from 'react-router-dom';
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

    const {bookId} = useParams();

    const username = localStorage.getItem('username');
    const accessToken = localStorage.getItem('accessToken');

    const navigate = useNavigate();
    
    async function loadbook() {
        try {
            const response = await api.get(`/books/${bookId}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            })
            let adjustedDate = response.data.launch_date.split("T", 10)[0];
            
            setId(response.data.id)
            setTitle(response.data.title);
            setAuthor(response.data.author);
            setPrice(response.data.price);
            setLaunch_date(adjustedDate);
        }catch(err) {
            alert("Error recoverng Book. Try again")
            navigate('/books')
        }
    }

    useEffect (() => {
        if (bookId == '0') return;
        else loadbook();
    }, {bookId})

    async function saveOrUpdate(e) { 
        e.preventDefault(); 
        
        const data = {
            title, author, price, launch_date, 
        }

        try {
            if (bookId == '0') {
                const response = await api.post('/books/createBook', data, {
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                    }
                });
            } else {
                data.id = id;
                const response = await api.put(`/books/${id}`, data, {
                    headers: {
                        Authorization: `Bearer ${accessToken}`,
                    }
                });
            }
        
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
                    <h1>{bookId == '0' ? 'Add' : 'Update'} New Book</h1>
                    <p>Enter the book information and click on {bookId == '0' ? "'Add'" : "'Update'"} ### {bookId}</p>
                    <Link className='back-link' to='/books'>
                        <FiArrowLeft size={16} color='#251FC5'></FiArrowLeft>
                        Home
                    </Link>
                </section>
                <form onSubmit={saveOrUpdate}>
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

                    <button className='button' type='submit'>{bookId == '0' ? 'Add' : 'Update'}</button>
                </form>
            </div>
        </div>
    );
}
