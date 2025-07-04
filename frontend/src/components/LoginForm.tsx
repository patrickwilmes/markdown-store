import * as React from "react";
import {useNavigate} from "react-router-dom";
import {useState} from "react";
import {loginUser} from "../api/ApiService.ts";

export const LoginForm = () => {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
    });

    const navigate = useNavigate();

    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<boolean>(false);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);
        setSuccess(false);
        try {
            await loginUser(formData);
            setSuccess(true);
            setTimeout(() => {
                navigate('/home');
            }, 100);
        } catch (error) {
            setError(error.message)
        }
    };

    return (
        <div className="max-w-md mx-auto mt-8 p-6 bg-white rounded-xl shadow-md">
            <h2 className="text-2xl font-bold mb-6 text-gray-800">Login</h2>

            {error && (
                <div className="mb-4 p-3 bg-red-100 text-red-700 rounded">
                    {error}
                </div>
            )}

            <form onSubmit={handleSubmit}>
                <div className="mb-4">
                    <label
                        htmlFor="username"
                        className="block text-gray-700 font-medium mb-2"
                    >
                        Username
                    </label>
                    <input
                        type="text"
                        id="username"
                        name="username"
                        value={formData.username}
                        onChange={handleChange}
                        className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-indigo-500"
                        required
                    />
                </div>

                <div className="mb-6">
                    <label
                        htmlFor="password"
                        className="block text-gray-700 font-medium mb-2"
                    >
                        Password
                    </label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-indigo-500"
                        required
                    />
                </div>

                <button
                    type="submit"
                    className="w-full bg-indigo-500 text-white py-2 px-4 rounded hover:bg-indigo-600 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
                >
                    Login
                </button>
                <a href="/register">Register</a>
            </form>
        </div>
    );
};
