import axios from 'axios';

const BASE_URL = 'http://localhost:8080/authenticate';

export async function login(email, password) {
  try {
    const response = await axios.post(`${BASE_URL}/login`, { email, password });
    return response.data;
  } catch (error) {
    console.error('Error login data:', error);
    throw new Error('Error fetching data. Please try again later.');
  }
}

export async function register(userData) {
  try {
    const response = await axios.post(`${BASE_URL}/register`, userData);
    return response.data;
  } catch (error) {
    console.error('Error login data:', error);
    throw new Error('Error fetching data. Please try again later.');
  }
}

export async function updateUser(userId, userData, token) {
  try {
    const response = await axios.put(`${BASE_URL}/update/${userId}`, userData, {
      headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
  } catch (error) {
    console.error('Error login data:', error);
    throw new Error('Error fetching data. Please try again later.');
  }
}

export async function deleteUser(userId, token) {
  try {
    const response = await axios.delete(`${BASE_URL}/delete/${userId}`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
  } catch (error) {
    console.error('Error login data:', error);
    throw new Error('Error fetching data. Please try again later.');
  }
}

export async function getAllUsers(token) {
  try {
    const response = await axios.get(`${BASE_URL}/getAllUsers`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
  } catch (error) {
    console.error('Error login data:', error);
    throw new Error('Error fetching data. Please try again later.');
  }
}

export function logout() {
  localStorage.removeItem('token');
  localStorage.removeItem('role');
}

export function isAuthenticated() {
  const token = localStorage.getItem('token');
  return !!token;
}
