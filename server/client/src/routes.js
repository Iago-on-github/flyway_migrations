import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';

import Login from './pages/Login';
import Books from './pages/Login/books';
import NewBook from './pages/Login/new book';

export default function AppRoutes() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" exact element={<Login />} />
                <Route path="/books" element={<Books />} />
                <Route path="/books/new" element={<NewBook />} />
            </Routes>
        </BrowserRouter>
    );
}