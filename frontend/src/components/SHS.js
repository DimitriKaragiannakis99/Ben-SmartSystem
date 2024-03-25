import React, { useEffect } from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogTitle from "@mui/material/DialogTitle";
import FormControlLabel from "@mui/material/FormControlLabel";
import Checkbox from "@mui/material/Checkbox";
import {
  DataGrid,
  GridToolbarContainer,
  useGridApiContext,
} from "@mui/x-data-grid";
import dayjs from "dayjs";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { DateTimeField } from "@mui/x-date-pickers/DateTimeField";

import axios from "axios";

// Data for the table\
//   TODO: Dinamically change this so we get all modules and permission types from backend
const columns = [
  { field: "id", headerName: "ID", width: 70 },
  { field: "name", headerName: "Name", width: 130 },
  // Permissions
  {
    field: "remoteAccess",
    headerName: "Remote Access",
    type: "boolean",
    width: 150,
  },
  {
    field: "lightAccess",
    headerName: "Light Access",
    type: "boolean",
    width: 150,
  },
  {
    field: "windowAccess",
    headerName: "Window Access",
    type: "boolean",
    width: 150,
  },
  {
    field: "doorAccess",
    headerName: "Door Access",
    type: "boolean",
    width: 150,
  },
];

//! Harcoded data for now
const rows = [
  {
    id: 1,
    name: "Parent",
    remoteAccess: true,
    doorAccess: true,
    windowAccess: true,
    lightAccess: true,
  },
  { id: 2, name: "Child", doorAccess: true },
];

//This just generates the user ID
function getUID() {
  // Get the timestamp and convert
  // it into alphanumeric input
  return Date.now().toString(36);
}

//TODO: Change all this so the logic is in the backend
//In here we have the functionality to add/remove and edit user profile information
//We will first add the user information
function AddUser(userName, permissions) {
  //We use the userName as the unique identifier for the user

  /*
The permissions are going to be a dictionary with the following structure:
    var permissions = 
    {
        "accessDoors": true,
        "accessWindows": false,
        "accessLights": false
    }
This means that the user can only open doors but not windows or lights
*/

  //We will just save the userName and the permissions in the local storage
  //I guess we will set the ID here by just using the length of the local storage
  let id = getUID();

  // This also adds it to the local storage
  localStorage.setItem(
    id,
    JSON.stringify({ name: userName, permissions: permissions })
  );

  // TODO: Check if this works, after doing the retreival from the backend
  //Add the user here to the backend using axios
  axios
    .post("http://localhost:8080/api/users/add", {
      id: id,
      username: userName,
      permissions: JSON.stringify(permissions),
    })
    .catch((error) => {
      console.error("Error sending User Info", error);
    });

  //Print to the console the user information
  // console.log("Added User: " + userName + "| Permissions: " + JSON.stringify(permissions));
  return true;
}

function RemoveUser(id) {
  //We will just remove the user from the local storage
  localStorage.removeItem(id);

  // We will also remove the user from the backend
  axios.get("http://localhost:8080/api/users/delete/" + id).catch((error) => {
    console.error("Error sending User Info", error);
  });
  //Print to the console the user information
  console.log("User: " + id + " has been removed");
  return true;
}

function EditUser(id, userName, permissions) {
  //We will just save the userName and the permissions in the local storage
  localStorage.setItem(
    id,
    JSON.stringify({ name: userName, permissions: permissions })
  );

  // Update in the backend
  axios
    .post("http://localhost:8080/api/users/update/" + id, {
      id: id,
      username: userName,
      permissions: JSON.stringify(permissions),
    })
    .catch((error) => {
      console.error("Error sending User Info", error);
    });
  //Print to the console the user information
  console.log(
    "Updated User ID: " + id + " Permissions: " + JSON.stringify(permissions)
  );
  return true;
}

function logAsUser(id, setter) {
  //We will just read the user from the local storage
  console.log("User: " + id + " has been logged in");
  setter(id);
}

function setHouseLocation(id, location) {}

function setSimulationTime(hour, day, month) {
  axios
    .post("http://localhost:8080/api/time/setTime", {
      hour: hour,
      day: day,
      month: month,
    })
    .catch((error) => {
      console.error("Error sending time Info", error);
    });
}

function UserManagementTab() {
  // Hooks for the current user
  const [currentUser, setCurrentUser] = React.useState("Simulator");

  // Hooks to read the users from the local storage
  const [users, setUsers] = React.useState([]);

  useEffect(() => {
    setUsers([...readUsers()]);
  }, []);

  // TODO: Fix this, sometimes it doesnt update the users
  function readUsers() {
    let users = [];
    // Instead of reading from the local storage, we will read from the backend
    // We will use axios to get the users from the backend
    axios.get("http://localhost:8080/api/users/all").then((response) => {
      // console.log(response.data);
      //Assign to the users variable the response from the backend
      for (let i = 0; i < response.data.length; i++) {
        // console.log(response.data[i].id);
        let id = response.data[i].id;
        let name = response.data[i].username;
        let permissions = response.data[i].permissions;
        // console.log(JSON.parse(permissions));
        let newPermissions = JSON.parse(permissions);

        // value = JSON.parse(permissions);
        let user = { id: id, name: name };
        let newUser = { ...user, ...newPermissions }; //Concatenate the user and the permissions
        //  console.log(newUser);
        // let newUser = {...user, ...value.permissions}; //Concatenate the user and the permissions
        users.push(newUser);
      }
      // console.log("users b: " + JSON.stringify(users));
      setUsers(users);
    });

}

export default function UserManagement() {
  return (
    //Just create a simple button that creates a user
    <div>
      <UserManagementTab />
    </div>
  );
}
