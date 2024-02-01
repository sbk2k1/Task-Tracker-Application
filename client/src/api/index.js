import axios from "axios";
import Cookies from "universal-cookie";
const cookies = new Cookies();

const BASE_URL = "http://localhost:8080/";

// ------------------ API CALLING ------------------ //
export const onPostData = async (url, data) => {
  // if token is present in cookies then add it to header
  if (cookies.get("data")) {
    return await axios.post(BASE_URL + url, data, {
      headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Methods": "POST",
        Authorization: "Bearer " + cookies.get("data").token,
      },
    });
  } else {
    return await axios.post(BASE_URL + url, data, {
      headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Methods": "POST",
      },
    });
  }
};

export const onGetData = async (url) => {
  return await axios.get(BASE_URL + url, {
    headers: {
      "Content-Type": "application/json",
      "Access-Control-Allow-Methods": "GET,PUT,POST,DELETE,PATCH,OPTIONS",
      Authorization: "Bearer " + cookies.get("data").token,
    },
  });
};

export const onPutData = async (url, data) => {
  return await axios.put(BASE_URL + url, data, {
    headers: {
      "Content-Type": "application/json",
      "Access-Control-Allow-Methods": "PUT",
      Authorization: "Bearer " + cookies.get("data").token,
    },
  });
};

export const onDeleteData = async (url) => {
  return await axios.delete(BASE_URL + url, {
    headers: {
      "Content-Type": "application/json",
      "Access-Control-Allow-Methods": "DELETE",
      Authorization: "Bearer " + cookies.get("data").token,
    },
  });
};

// ------------------ AUTHENTICATION ------------------ //

// set encrypted data in cookie
export const setData = (data) => {
  cookies.set("data", data, { path: "/" });
};

// remove data from cookie
export const removeData = () => {
  cookies.remove("data", { path: "/" });
  window.location.reload();
};

// method to check if user is logged in
export const isUser = () => {
  if (cookies.get("data")) {
    return true;
  } else {
    return false;
  }
};

// get username from cookie
export const getUsername = () => {
  if (cookies.get("data")) {
    return cookies.get("data").username;
  } else {
    return null;
  }
};
