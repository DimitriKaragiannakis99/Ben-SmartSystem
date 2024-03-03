import React from "react";

//In here we have the functionality to add/remove and edit user profile information
//We will first add the user information
function AddUser(userName, permissions) {
//We use the userName as the unique identifier for the user

/*
The permissions are going to be a dictionary with the following structure:
    var permissions = 
    {
        "useDoors": true,
        "useWindows": false,
        "useLights": false
    }
This means that the user can only open doors but not windows or lights
*/

//We will just save the userName and the permissions in the local storage
localStorage.setItem(userName, JSON.stringify(permissions));
//Print to the console the user information
console.log("User: " + userName + " Permissions: " + JSON.stringify(permissions));
return true;
}

function RemoveUser(userName) {
    //We will just remove the user from the local storage
    localStorage.removeItem(userName);
    //Print to the console the user information
    console.log("User: " + userName + " has been removed");
    return true;
}

function EditUser(userName, permissions) {
    //We will just save the userName and the permissions in the local storage
    localStorage.setItem(userName, JSON.stringify(permissions));
    //Print to the console the user information
    console.log("Updated User: " + userName + " Permissions: " + JSON.stringify(permissions));
    return true;
}

function TempButton() {
    return (
        <div>
            <div>
            <button onClick={() => AddUser("John", {"useDoors": true, "useWindows": false, "useLights": false})}>Add User</button>
            </div>
            <div> 
            <button onClick={() => RemoveUser("John")}>Remove User</button>
            </div>
            <div> 
            <button onClick={() => EditUser("John", {"useDoors": true, "useWindows": true, "useLights": true})}>Edit User</button>
            </div>
        </div>
    );
}

export default function UserManagement() {
    return (
    //Just create a simple button that creates a user
    <div>
        <TempButton />
    </div>
  );
}