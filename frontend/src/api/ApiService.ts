const API_BASE_URL = 'http://localhost:8080';

export interface UserData {
    username: string;
    password: string;
}

export const registerUser = async (userData: UserData): Promise<Response> => {
    const response = await fetch(`${API_BASE_URL}/user/register`, {
        method: 'POST',
        body: JSON.stringify(userData),
        headers: {
            'Content-Type': 'application/json'
        },
    });
    console.log(response);
    if (response.status != 201) {
        throw new Error('Failed to register user');
    }
    return response;
}

export const loginUser = async (userData: UserData): Promise<Response> => {
    const response = await fetch(`${API_BASE_URL}/user/login`, {
        method: 'POST',
        body: JSON.stringify(userData),
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include',
    });
    console.log(response);
    if (!response.ok) {
        throw new Error('Failed to login user');
    }
    return response;
};

export const authMe = async (): Promise<Response> => {
    const response = await fetch(`${API_BASE_URL}/user/auth-me`, {
        method: 'GET',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json'
        },
    });
    console.log(response);
    if (!response.ok) {
        throw new Error('Failed to auth user');
    }
    return response;
};
