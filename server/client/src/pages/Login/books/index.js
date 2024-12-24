import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FiPower, FiEdit, FiTrash2 } from 'react-icons/fi';

import api from '../../../services/api';

import './styles.css';

import Logo from '../../../asset/logo.svg';

export default function Books() {

    const[books, setBooks] = useState([]);
    const[page, setPage] = useState(1);

    const username = localStorage.getItem('username');
    const accessToken = localStorage.getItem('accessToken');

    const navigate = useNavigate();

    async function logout() {
        localStorage.clear();
        navigate('/');   
    }

    async function editBook(id) {
        try {
            navigate(`/books/new/${id}`);
        } catch(err) {
            alert('Delete book failed. Try again.')
        }
    }

    async function deleteBook(id) {
        try {
            await api.delete(`/books/${id}`,{
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            })
            setBooks(books.filter(book => book.id !== id))
        } catch(err) {
            alert('Delete failed! try again.');
        }
    }

    async function fetchMoreBooks() {
        const response = await api.get('/books', {
            headers: {
                Authorization: `Bearer ${accessToken}`
            },
            params: {
                page: page,
                limit: 4,
                direction: 'asc'
            } 
        })

        setBooks([ ...books,...response.data._embedded.bookList]);
        setPage(page + 1);
    }

    useEffect(() => {
        fetchMoreBooks();
    }, []);

    return (
        <div className='book-container'>
            <header>
                <img src={Logo} alt='logo'></img>
                <span>Welcome, <strong>{username.toUpperCase()}</strong>!</span>
                <Link className='button' to='/books/new/0'>Add New Book</Link>
                <button onClick={logout} type='button'>
                    <FiPower size={18} color='#251FC5'/>
                </button>
            </header>

            <h1>Registered Books</h1>

    <ul>
        {books.map((b, index) => (
            <li key={b.id || index}>
            <strong>Title:</strong>
            <p>{b.title}</p>

            <strong>Author:</strong>
            <p>{b.author}</p>

            <strong>Price:</strong>
            <p>{Intl.NumberFormat('pt-BR', {style: 'currency', currency: 'BRL'}).format(b.price)}</p>

            <strong>Release Date:</strong>
            <p>{Intl.DateTimeFormat('pt-BR').format(new Date(b.launch_date))}</p>

            <button onClick={() => editBook(b.id)} type="button">
                <FiEdit size={20} color="#251FC5" />
            </button>
            <button onClick={() => deleteBook(b.id)} type="button">
                <FiTrash2 size={20} color="#251FC5" />
            </button>
        </li>
        ))}
        </ul>
        <button className='button' onClick={fetchMoreBooks} type='button'>Load More</button>
        </div>
    );
}