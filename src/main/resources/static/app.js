$(async function () {
     await getTableWithUsers();
     await showLoggedInUser()
});

const url = 'http://localhost:8080/api/users';
const container = document.getElementById('AllUsersTable')
let output = '';
const name = document.getElementById('name')

const userFetchService = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null
    },
    findAllUsers: async () => await fetch('api/users'),
    showSingleUser: async () => await fetch('api/userView'),
    findOneUser: async (id) => await fetch(`api/users/${id}`),
    addNewUser: async (user) => await fetch('api/users', {
        method: 'POST',
        headers: userFetchService.head,
        body: JSON.stringify(user)
    }),
    updateUser: async (user) => await fetch(`api/users`, {
        method: 'PATCH',
        headers: userFetchService.head,
        body: JSON.stringify(user)
    }),
    deleteUser: async (id) => await fetch(`api/users/${id}`, {method: 'DELETE', headers: userFetchService.head})
}

async function getTableWithUsers() {
    let table = $('#AllUsersTable');
    table.empty();
    await userFetchService.findAllUsers()
        .then(res => res.json())
        .then(users => {
            users.forEach(user => {
                let tableFilling = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.name}</td>
                            <td>${user.lastName}</td>
                            <td>${user.age}</td>
                            <td>${user.username}</td>    
                            <td>${user.roles.map(role => " " + role.roleName.substring(5))}</td>         
                            <td>
                                <button type="button" onclick="editUser(${user.id})" data-action="edit" class="btn btn-info" 
                                data-toggle="modal" data-target="#edit">Edit</button>
                            </td>
                            <td>
                                <button type="button" onclick="deleteUser(${user.id})" data-action="delete" class="btn btn-danger" 
                                data-toggle="modal" data-target="#delete">Delete</button>
                            </td>
                        </tr>
                )`;
                table.append(tableFilling);
            })
        })
}

async function showLoggedInUser() {
    userFetchService.showSingleUser()
        .then(res => res.json())
        .then(data => {
            let user = `$(
            <tr>
                <td>${data.id}</td>
                <td>${data.name}</td>
                <td>${data.lastName}</td>
                <td>${data.age}</td>
                <td>${data.username}</td>
                <td>${data.roles.map(role => " " + role.roleName.substring(5))}</td>)`;
            $('#singleUserTable').append(user);
        })
}

async function editUser(id) {
    userFetchService.findOneUser(id)
        .then(res => {
            res.json().then(user => {
                $('#editId').val(user.id)
                $('#editName').val(user.name)
                $('#editLastName').val(user.lastName)
                $('#editAge').val(user.age)
                $('#editEmail').val(user.username)
                $('#editPassword').val("")
                $('#editRoles').val(user.roles)
            })
        })
}

function updateUser() {
    let user = {
        id: document.getElementById('editId').value,
        name: document.getElementById('editName').value,
        lastName: document.getElementById('editLastName').value,
        age: document.getElementById('editAge').value,
        username: document.getElementById('editEmail').value,
        password: document.getElementById('editPassword').value,
         // roles: [document.getElementById('editRoles').value]
    };
    document.forms["editForm"].addEventListener("submit", ev => {
        ev.preventDefault();
        userFetchService.updateUser(user).then(() => {
            getTableWithUsers()
            $('#editModalCloseButton').click();
        })
    })
}


function deleteUser(id) {
    userFetchService.findOneUser(id)
        .then(res => {
            res.json().then(user => {
                $('#deleteId').val(user.id)
                $('#deleteName').val(user.name)
                $('#deleteLastName').val(user.lastName)
                $('#deleteAge').val(user.age)
                $('#deleteEmail').val(user.username)
                $('#deleteRoles').val(user.role)
            })
        })
}

function deleteUserById() {
    let id = document.getElementById('deleteId').value;
    document.forms["deleteForm"].addEventListener("submit", ev => {
        ev.preventDefault();
        userFetchService.deleteUser(id)
            .then(() => {
                getTableWithUsers()
                $('#deleteModalCloseButton').click();
            })
    })
}

function addUser() {
    let user = {
        name: document.getElementById('addName').value,
        lastName: document.getElementById('addLastName').value,
        age: document.getElementById('addAge').value,
        username: document.getElementById('addEmail').value,
        password: document.getElementById('addPassword').value,
        // roles: document.getElementById('addRoles').value;
    };
    document.forms["newUserForm"].addEventListener("submit", ev => {
        ev.preventDefault();
        userFetchService.addNewUser(user)
            .then(() => {
                document.forms["newUserForm"].reset();
                getTableWithUsers();
                $('#nav-tab li:first-child a').tab('show')
            })
    })
}

// добавить роли
// исправить дублирование таблицы при операциях