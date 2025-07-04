import {useEffect} from "react";
import {authMe} from "../api/ApiService.ts";
import {useNavigate} from "react-router-dom";

export const Dashboard = () => {
    const navigate = useNavigate();
    useEffect(() => {
        const verifyAuth = async() => {
            try {
                await authMe();
            } catch (error) {
                navigate('/login');
            }
        };
        verifyAuth();
    }, [navigate]);

    return (
        <>Accessing me requires authentication</>
    )
}
