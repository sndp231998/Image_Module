import { useEffect, useState } from "react";
import {
  uploadImage,
  listImages,
  getImageUrl,
  deleteImage,
} from "./API/imageApi";

function App() {
  const [file, setFile] = useState(null);
  const [images, setImages] = useState([]);

  // Load images on start
  const loadImages = async () => {
    listImages().then((response)=>{
      console.log(response.data);
      setImages(response.data);
    }).catch((error)=>{
      console.log(error);
      alert("Unable to get");
    });
  };

  useEffect(() => {
    loadImages();
  }, []);

  // Upload handler
  const handleUpload = async () => {
    if (!file) return alert("Select an image first");

    try {
      await uploadImage(file).then((response)=>{
        //to view the result fileName, file URL, size, bucket name, upload timestamp
        console.log(response);
      }).catch((error)=>{
        console.log(error);
      });
      setFile(null);
      loadImages();
      alert("Image uploaded successfully!");
    } catch (error) {
      alert("Upload failed!");
    }
  };

  // Delete handler
  const handleDelete = async (name) => {
    var a=window.confirm("Are you want to delete ?");
    if(a){

      await deleteImage(name).then((response)=>{
        alert("Image Deleted Successfully.");
      }).catch((error)=>{
        alert("Unable to delete.");
        console.log(error);
      });
    loadImages();
  };
  }

  return (
    <div style={styles.container}>
      <h1 style={styles.heading}>MinIO Image Manager</h1>

      {/* Upload Section */}
      <div style={styles.uploadBox}>
        <input
          type="file"
          accept="image/*"
          onChange={(e) => setFile(e.target.files[0])}
        />
        <button onClick={handleUpload} style={styles.btnPrimary}>
          Upload
        </button>
      </div>

      {/* Image List */}
      <h2>Uploaded Images</h2>
      <div style={styles.grid}>
        {images && images.map((img, index) => (
          <div key={index} style={styles.card}>
            <img
              src={getImageUrl(img.name)}
              alt="img"
              style={styles.image}
            />

            <p><b>{img.name}</b></p>
            <p>{(img.size / 1024).toFixed(2)} KB</p>

            <button
              style={styles.btnDelete}
              onClick={() => handleDelete(img.name)}
            >
              Delete
            </button>
          </div>
        ))}
      </div>

    </div>
  );
}

export default App;

// Simple styling
const styles = {
  container: { padding: "20px", fontFamily: "Arial" },
  heading: { marginBottom: "20px" },
  uploadBox: { marginBottom: "20px" },
  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fill, minmax(200px, 1fr))",
    gap: "20px",
  },
  card: {
    padding: "15px",
    border: "1px solid #ddd",
    borderRadius: "10px",
    textAlign: "center",
  },
  image: { width: "100%", height: "150px", objectFit: "cover" },
  btnPrimary: {
    marginLeft: "10px",
    padding: "8px 15px",
    cursor: "pointer",
    background: "#4c7cf3",
    color: "white",
    border: "none",
    borderRadius: "6px",
  },
  btnDelete: {
    marginTop: "10px",
    padding: "6px 12px",
    background: "red",
    color: "white",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
  },
};
