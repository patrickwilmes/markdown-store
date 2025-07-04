import './App.css'
import {LoginForm} from './components/LoginForm'
import {RegistrationForm} from './components/RegistrationForm'
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {Dashboard} from "./components/Dashboard";

function App() {
    return (
        <BrowserRouter>
            <div className="min-h-screen bg-gray-100">
                <Routes>
                    <Route path="/login" element={<LoginForm/>}/>
                    <Route path="/register" element={<RegistrationForm/>}/>
                    <Route path="/" element={
                        <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
                            <h1 className="text-3xl font-bold">Welcome to the Main Page</h1>
                            <a href="/login">Login</a>
                        </div>
                    }/>
                    <Route path="/home" element={
                        <Dashboard/>
                    }/>
                </Routes>
            </div>
        </BrowserRouter>
    )
}

export default App
