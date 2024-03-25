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

    return users;
    //We will just read all the users from the local storage
  }

  // Hooks for the dialog form for creating a new user
  const [open, setOpen] = React.useState(false);
  const handleClickOpen = () => {
    setOpen(true);
  };
  const handleClose = () => {
    setOpen(false);
  };

  // Hooks for the dialog form for editing a user
  const [openE, setOpenE] = React.useState(false);
  const [currentID, setCurrentID] = React.useState("");
  const handleClickOpenE = (ID) => {
    setOpenE(true);
    setCurrentID(ID);
  };
  const handleCloseE = () => {
    setOpenE(false);
  };

  // Hooks for the dialog form for changing the time
  const [openT, setOpenT] = React.useState(false);
  const handleClickOpenT = () => {
    setOpenT(true);
  };
  const handleCloseT = () => {
    setOpenT(false);
  };

  let selectedIds = [];

  const onRowsSelectionHandler = (ids) => {
    // This is all the data in case it is needed
    let selectedRowsData = ids.map((id) => users.find((row) => row.id === id));
    //Print all the IDS
    selectedIds = ids;
    //  console.log(selectedIds);
    //  console.log(selectedRowsData);
  };

  function removeAllSelectedUsers(selectedIds) {
    for (let i = 0; i < selectedIds.length; i++) {
      RemoveUser(selectedIds[i]);
    }

    //Update the UI
    readUsers();
  }

  function editSelectedUser(selectedIds) {
    console.log(selectedIds);
    if (selectedIds.length > 1) {
      console.log("You can only edit one user at a time");
      //Open a dialog box that says that you can only edit one user at a time
      alert("You can only edit one user at a time");
    } else if (selectedIds.length === 0) {
      console.log("You need to select a user to edit");
      //Open a dialog box that says that you need to select a user to edit
      alert("You need to select a user to edit");
    } else {
      handleClickOpenE(selectedIds[0]);
    }
  }

  function logAsSelectedUser(selectedIds) {
    console.log(selectedIds);
    if (selectedIds.length > 1) {
      console.log("You can only login as one user at a time");
      //Open a dialog box that says that you can only edit one user at a time
      alert('You can only login as one user at a time"');
    } else if (selectedIds.length === 0) {
      console.log("No user selected, changing back to default user");
      //Open a dialog box that says that you need to select a user to edit
      alert("No user selected, changing back to default user");
      logAsUser("Simulator", setCurrentUser);
    } else {
      //In here we will just open the dialog box with the user information
      logAsUser(selectedIds[0], setCurrentUser);
      localStorage.setItem("currentUserId", selectedIds[0]);
    }
  }

  return (
    <div className="container bg-blue-500 mx-auto my-8 p-4">
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
        {/* Containers for user management */}
        <div className="md:col-span-3 md:order-1">
          {/* Box for house components */}
          <div className="mb-4 p-4 border border-gray-200 rounded">
            <h2 className="font-bold mb-3">Users and Module Permissions</h2>
            <h3>Current User: {currentUser} </h3>
            <br />
            {/* Table of all users */}
            <div style={{ height: 400, width: "100%" }}>
              <DataGrid
                rows={users}
                columns={columns}
                initialState={{
                  pagination: {
                    paginationModel: { page: 0, pageSize: 5 },
                  },
                }}
                pageSizeOptions={[5, 10]}
                checkboxSelection
                onRowSelectionModelChange={(ids) => onRowsSelectionHandler(ids)}
              />
            </div>

            <div>
              <button
                className="px-4 py-2 mt-2 border border-gray-300 bg-gray-500 text-white rounded hover:bg-gray-700 transition-colors mr-2"
                onClick={() => logAsSelectedUser(selectedIds)}
              >
                Log as selected
              </button>

              <button
                className="px-4 py-2 mt-2 border border-gray-300 bg-green-500 text-white rounded hover:bg-green-700 transition-colors mr-2"
                onClick={handleClickOpen}
              >
                Add New
              </button>
              {/* The Dialog Form for Adding a User */}
              <Dialog
                open={open}
                onClose={handleClose}
                PaperProps={{
                  component: "form",
                  onSubmit: (event) => {
                    event.preventDefault();
                    const formData = new FormData(event.currentTarget);
                    const formJson = Object.fromEntries(formData.entries());
                    //In here we will send handle the data
                    const userName = formJson.name;
                    // Here we will remove the name from the formJson
                    delete formJson.name;
                    const permissions = formJson;
                    AddUser(userName, permissions);
                    readUsers();
                    handleClose();
                  },
                }}
              >
                <DialogTitle>Add User</DialogTitle>
                <DialogContent>
                  <TextField
                    autoFocus
                    required
                    margin="dense"
                    id="name"
                    name="name"
                    label="User's Name:"
                    fullWidth
                    variant="standard"
                  />
                  {/* Permissions's part of the form */}
                  {/* //TODO: Dinamically update this too */}
                  <FormControlLabel
                    control={<Checkbox />}
                    value={true}
                    name="remoteAccess"
                    label="Remote Access"
                  />
                  <FormControlLabel
                    control={<Checkbox />}
                    value={true}
                    name="lightAccess"
                    label="Light Access"
                  />
                  <FormControlLabel
                    control={<Checkbox />}
                    value={true}
                    name="windowAccess"
                    label="Window Access"
                  />
                  <FormControlLabel
                    control={<Checkbox />}
                    value={true}
                    name="doorAccess"
                    label="Door Access"
                  />
                </DialogContent>
                <DialogActions>
                  <Button onClick={handleClose}>Cancel</Button>
                  <Button type="submit">Add</Button>
                </DialogActions>
              </Dialog>

              {/* The Dialog Form for Editing a User */}
              <Dialog
                open={openE}
                onClose={handleCloseE}
                PaperProps={{
                  component: "form",
                  onSubmit: (event) => {
                    event.preventDefault();
                    const formData = new FormData(event.currentTarget);
                    const formJson = Object.fromEntries(formData.entries());
                    //In here we will send handle the data
                    const userName = formJson.name;
                    // Here we will remove the name from the formJson
                    delete formJson.name;
                    const permissions = formJson;
                    const id = currentID;
                    EditUser(id, userName, permissions);
                    readUsers();
                    handleCloseE();
                  },
                }}
              >
                <DialogTitle>Edit User</DialogTitle>
                <DialogContent>
                  <TextField
                    autoFocus
                    required
                    margin="dense"
                    label="User's ID:"
                    id="ID"
                    disabled
                    fullWidth
                    variant="standard"
                    value={currentID}
                  />
                  <TextField
                    autoFocus
                    required
                    margin="dense"
                    id="name"
                    name="name"
                    label="New User's Name:"
                    fullWidth
                    variant="standard"
                  />
                  {/* Permissions's part of the form */}
                  {/* //TODO: Dinamically update this too */}
                  <FormControlLabel
                    control={<Checkbox />}
                    value={true}
                    name="remoteAccess"
                    label="Remote Access"
                  />
                  <FormControlLabel
                    control={<Checkbox />}
                    value={true}
                    name="lightAccess"
                    label="Light Access"
                  />
                  <FormControlLabel
                    control={<Checkbox />}
                    value={true}
                    name="windowAccess"
                    label="Window Access"
                  />
                  <FormControlLabel
                    control={<Checkbox />}
                    value={true}
                    name="doorAccess"
                    label="Door Access"
                  />
                </DialogContent>
                <DialogActions>
                  <Button onClick={handleCloseE}>Cancel</Button>
                  <Button type="submit">Add</Button>
                </DialogActions>
              </Dialog>

              {/* The Dialog Form for changing the Time */}
              <Dialog
                open={openT}
                onClose={handleCloseT}
                PaperProps={{
                  component: "form",
                  onSubmit: (event) => {
                    event.preventDefault();
                    const formData = new FormData(event.currentTarget);
                    const formJson = Object.fromEntries(formData.entries());
                    //In here we will send handle the data
                    const text = formJson.dateTime.toString();
                    console.log(formJson.dateTime.toString());
                    let day = text.split("/")[1];
                    let month = text.split("/")[0];
                    let hour = text.split("/")[2].split(" ")[1].split(":")[0];

                    //  console.log("Day: " + day + " Month: " + month + " Year: " + year + " Time: " + time);

                    setSimulationTime(hour, day, month);
                    handleCloseT();
                  },
                }}
              >
                <DialogTitle>Change Simulation Date/Time</DialogTitle>
                <DialogContent>
                  <LocalizationProvider dateAdapter={AdapterDayjs}>
                    <DemoContainer
                      components={[
                        "DateTimeField",
                        "DateTimeField",
                        "DateTimeField",
                      ]}
                    >
                      <DateTimeField
                        label="Enter Date and Time"
                        format="L HH:mm"
                        name="dateTime"
                        id="dateTime"
                        required
                      />
                    </DemoContainer>
                  </LocalizationProvider>
                </DialogContent>
                <DialogActions>
                  <Button onClick={handleCloseE}>Cancel</Button>
                  <Button type="submit">Add</Button>
                </DialogActions>
              </Dialog>

              <button
                className="px-4 py-2 mt-2 border border-gray-300 bg-green-500 text-white rounded hover:bg-green-700 transition-colors mr-2"
                onClick={() => editSelectedUser(selectedIds)}
              >
                Edit Selected
              </button>

              <button
                className="px-4 py-2 mt-2 border border-gray-300 bg-red-500 text-white rounded hover:bg-red-700 transition-colors mr-2"
                onClick={() => removeAllSelectedUsers(selectedIds)}
              >
                Delete Selected
              </button>
              <br />
              <button
                className="px-4 py-2 mt-2 border border-gray-300 bg-gray-500 text-white rounded hover:bg-gray-700 transition-colors mr-2"
                onClick={handleClickOpenT}
              >
                Set Simulation Time
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default function UserManagement() {
  return (
    //Just create a simple button that creates a user
    <div>
      <UserManagementTab />
    </div>
  );
}
