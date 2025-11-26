import axios from "axios";

const API_BASE = "http://localhost:8080/image";

const api = axios.create({
  baseURL: API_BASE,
});

// UPLOAD Image
export const uploadImage = async (file) => {
  const formData = new FormData();
  formData.append("file", file);

  const res = await api.post("/upload", formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });

  return res.data;
};

// LIST Images
export const listImages = async () => {
  return (
    axios.get(`http://localhost:8080/image/list`)
  );
};

// GET / VIEW Image
export const getImageUrl = (fileName) => {
  return `${API_BASE}/${fileName}`;
};

// DELETE image
export const deleteImage = async (fileName) => {
  return (  api.delete(`/${fileName}`,));
};
