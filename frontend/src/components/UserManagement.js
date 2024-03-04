import React, { useEffect } from "react";
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import { DataGrid, GridToolbarContainer, useGridApiContext } from '@mui/x-data-grid';

// Data for the table\
//   TODO: Dinamically change this so we get all modules and permission types from backend
const columns = [
    { field: 'id', headerName: 'ID', width: 70 },
    { field: 'name', headerName: 'Name', width: 130 },
    // Permissions
    {
      field: 'remoteAccess',
      headerName: 'Remote Access',
      type: 'boolean',
      width: 150,
    },
    {
        field: 'lightAccess',
        headerName: 'Light Access',
        type: 'boolean',
        width: 150,
      },
      {
        field: 'windowAccess',
        headerName: 'Window Access',
        type: 'boolean',
        width: 150,
      },
      {
        field: 'doorAccess',
        headerName: 'Door Access',
        type: 'boolean',
        width: 150,
      }
  ];

//! Harcoded data for now
const rows = [
    {id: 1,name: 'Parent', remoteAccess: true, doorAccess: true, windowAccess: true, lightAccess: true},
    {id: 2, name: 'Child', doorAccess: true}
  ];

  //This just generates the user ID
function getUID() {
    // Get the timestamp and convert 
    // it into alphanumeric input
    return Date.now().toString(36);
    }

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
// !This is not ideal since the local storage can be used for other things too
let id = getUID();

localStorage.setItem(id, JSON.stringify({name: userName, permissions:(permissions)}));

//Print to the console the user information
console.log("Added User: " + userName + "| Permissions: " + JSON.stringify(permissions));
return true;
}

function RemoveUser(id) {
    //We will just remove the user from the local storage
    localStorage.removeItem(id);
    //Print to the console the user information
    console.log("User: " + id + " has been removed");
    return true;
}

function EditUser(userName, permissions) {
    //We will just save the userName and the permissions in the local storage
    localStorage.setItem(+userName, JSON.stringify(permissions));
    //Print to the console the user information
    console.log("Updated User: " + userName + " Permissions: " + JSON.stringify(permissions));
    return true;
}

function readUsers() {
    //We will just read all the users from the local storage
    let users = [];
    for (let i = 0; i < localStorage.length; i++){
        let id = localStorage.key(i);
        let value = localStorage.getItem(id);
        value = JSON.parse(value);
        let user= {id: id, name: value.name};
        let newUser = {...user, ...value.permissions}; //Concatenate the user and the permissions
        users.push(newUser);
    }
    console.log("Users: " + JSON.stringify(users));
    return users;
}




function UserManagementTab() {

    // Hooks to read the users from the local storage
    const [users, setUsers] = React.useState([]);

    useEffect(() => {
        setUsers([...readUsers()]);
    }, []);
    

    // Hooks for the dialog form
    const [open, setOpen] = React.useState(false);
    const handleClickOpen = () => {
      setOpen(true);
    };
    const handleClose = () => {
      setOpen(false);
    };

    let selectedIds = [];
   
    const onRowsSelectionHandler = (ids) => {
        // This is all the data in case it is needed
        const selectedRowsData = ids.map((id) => users.find((row) => row.id === id));
        //Print all the IDS
        selectedIds=ids;
        console.log(selectedIds);
        // console.log(selectedRowsData);
      };

      function removeAllSelectedUsers(selectedIds)
    {
        for (let i = 0; i < selectedIds.length; i++)
        {
            RemoveUser(selectedIds[i]);
        }

        //Update the UI
        setUsers(readUsers());

    }

    return (
        <div className="container bg-blue-500 mx-auto my-8 p-4">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
                {/* Containers for user management */}
                <div className="md:col-span-3 md:order-1">
                    {/* Box for house components */}
                    <div className="mb-4 p-4 border border-gray-200 rounded">
                        <h2 className="font-bold mb-3">Users and Module Permissions</h2>
                    {/* Table of all users */}
                        <div style={{ height: 400, width: '100%' }}>
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
							className="px-4 py-2 mt-2 border border-gray-300 bg-green-500 text-white rounded hover:bg-green-700 transition-colors mr-2"
							onClick={handleClickOpen}>
							Add New
							</button>
                             {/* The Dialog Form */}
                             <Dialog
                                open={open}
                                onClose={handleClose}
                                PaperProps={{
                                component: 'form',
                                onSubmit: (event) => {
                                    event.preventDefault();
                                    const formData = new FormData(event.currentTarget);
                                    const formJson = Object.fromEntries(formData.entries());
                                    //In here we will send handle the data
                                    const userName=formJson.name;
                                    // Here we will remove the name from the formJson
                                    delete formJson.name;
                                    const permissions = formJson;
                                    AddUser(userName, permissions);
                                    setUsers(readUsers());
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
                                    <FormControlLabel control={<Checkbox />} value={true} name="remoteAccess" label="Remote Access" />
                                    <FormControlLabel control={<Checkbox />} value={true} name="lightAccess" label="Light Access" />
                                    <FormControlLabel control={<Checkbox />} value={true} name="windowAccess" label="Window Access" />
                                    <FormControlLabel control={<Checkbox />} value={true} name="doorAccess" label="Door Access" />
                            
                                </DialogContent>
                                <DialogActions>
                                <Button onClick={handleClose}>Cancel</Button>
                                <Button type="submit">Add</Button>
                                </DialogActions>
                            </Dialog>


                            <button
							className="px-4 py-2 mt-2 border border-gray-300 bg-green-500 text-white rounded hover:bg-green-700 transition-colors mr-2"
							onClick={() => handleOpenCloseAction("open")}>
							Edit Selected
							</button>

                            <button
							className="px-4 py-2 mt-2 border border-gray-300 bg-red-500 text-white rounded hover:bg-red-700 transition-colors mr-2"
							onClick={() => removeAllSelectedUsers(selectedIds)}> 
							Delete Selected
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