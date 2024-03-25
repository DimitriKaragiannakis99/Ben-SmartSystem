import axios from "axios";

const HomeLayout = ({ onSubmission }) => {
  const handleFileUpload = async (event) => {
    const file = event.target.files[0];

    //Handle file
    const formData = new FormData();
    formData.append("file", file);

    //Send POST request to backend with file info
    //Not using useEffect because there are no changes to DOM.
    try {
      const response = await axios.post(
        "http://localhost:8080/api/uploadRoomLayout",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );

      // Handle response from backend
      onSubmission(response.data); //Pass the data to the callback function
    } catch (error) {
      console.error("Error uploading file:", error);
    }
  };

  const handleUploadButtonClick = () => {
    document.getElementById("fileInput").click();
  };

  return (
    <div className="bg-blue-100 h-screen flex justify-center items-center">
      <div className="bg-purple-300 p-6 rounded-lg w-108 h-50 grid place-items-center">
        <p className="text-3xl mb-4">Submit Home Layout </p>
        <input
          type="file"
          accept=".txt"
          onChange={handleFileUpload}
          className="hidden"
          id="fileInput"
        />
        <button
          onClick={handleUploadButtonClick}
          className="bg-purple-400 hover:bg-purple-600 text-white font-bold py-2 px-4 rounded"
        >
          Upload File
        </button>
      </div>
    </div>
  );
};

export default HomeLayout;
