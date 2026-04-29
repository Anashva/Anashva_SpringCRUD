const apiUrl = '/students';
const form = document.getElementById('student-form');
const idField = document.getElementById('student-id');
const nameField = document.getElementById('student-name');
const emailField = document.getElementById('student-email');
const courseField = document.getElementById('student-course');
const messageBox = document.getElementById('form-message');
const tableBody = document.getElementById('student-table-body');
const tableMessage = document.getElementById('table-message');
const refreshButton = document.getElementById('refresh-button');
const cancelButton = document.getElementById('cancel-button');
const formTitle = document.getElementById('form-title');
const submitButton = document.getElementById('submit-button');

window.addEventListener('DOMContentLoaded', () => {
  loadStudents();
});

form.addEventListener('submit', async (event) => {
  event.preventDefault();
  const student = {
    name: nameField.value.trim(),
    email: emailField.value.trim(),
    course: courseField.value.trim(),
  };

  if (!student.name || !student.email || !student.course) {
    showMessage(messageBox, 'All fields are required.', 'error');
    return;
  }

  if (idField.value) {
    await updateStudent(parseInt(idField.value, 10), student);
  } else {
    await createStudent(student);
  }
});

refreshButton.addEventListener('click', loadStudents);
cancelButton.addEventListener('click', resetForm);

async function loadStudents() {
  try {
    setLoading(true);
    const response = await fetch(apiUrl);
    if (!response.ok) {
      throw new Error('Unable to load students.');
    }

    const students = await response.json();
    tableBody.innerHTML = '';
    if (students.length === 0) {
      tableMessage.textContent = 'No students found. Add one with the form above.';
      return;
    }

    tableMessage.textContent = '';
    for (const student of students) {
      const row = document.createElement('tr');
      row.innerHTML = `
        <td>${student.id}</td>
        <td>${student.name}</td>
        <td>${student.email}</td>
        <td>${student.course}</td>
        <td class="actions">
          <button class="action-button" onclick="editStudent(${student.id})">Edit</button>
          <button class="action-button delete" onclick="deleteStudent(${student.id})">Delete</button>
        </td>
      `;
      tableBody.appendChild(row);
    }
  } catch (error) {
    tableMessage.textContent = error.message;
    tableMessage.className = 'message error';
  } finally {
    setLoading(false);
  }
}

async function createStudent(student) {
  try {
    setLoading(true);
    const response = await fetch(apiUrl, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(student),
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || 'Failed to create student.');
    }

    showMessage(messageBox, 'Student created successfully.', 'success');
    resetForm();
    loadStudents();
  } catch (error) {
    showMessage(messageBox, error.message, 'error');
  } finally {
    setLoading(false);
  }
}

async function updateStudent(id, student) {
  try {
    setLoading(true);
    const response = await fetch(`${apiUrl}/${id}`, {
      method: 'PUT',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(student),
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || 'Failed to update student.');
    }

    showMessage(messageBox, 'Student updated successfully.', 'success');
    resetForm();
    loadStudents();
  } catch (error) {
    showMessage(messageBox, error.message, 'error');
  } finally {
    setLoading(false);
  }
}

async function deleteStudent(id) {
  if (!confirm('Delete this student?')) {
    return;
  }

  try {
    setLoading(true);
    const response = await fetch(`${apiUrl}/${id}`, {
      method: 'DELETE',
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || 'Failed to delete student.');
    }

    showMessage(messageBox, 'Student deleted successfully.', 'success');
    resetForm();
    loadStudents();
  } catch (error) {
    showMessage(messageBox, error.message, 'error');
  } finally {
    setLoading(false);
  }
}

function editStudent(id) {
  fetch(`${apiUrl}/${id}`)
    .then((response) => {
      if (!response.ok) {
        throw new Error('Unable to fetch student details.');
      }
      return response.json();
    })
    .then((student) => {
      idField.value = student.id;
      nameField.value = student.name;
      emailField.value = student.email;
      courseField.value = student.course;
      formTitle.textContent = 'Edit Student';
      submitButton.textContent = 'Update Student';
      messageBox.textContent = '';
    })
    .catch((error) => {
      showMessage(tableMessage, error.message, 'error');
    });
}

function resetForm() {
  idField.value = '';
  nameField.value = '';
  emailField.value = '';
  courseField.value = '';
  formTitle.textContent = 'Create a New Student';
  submitButton.textContent = 'Save Student';
  messageBox.textContent = '';
}

function showMessage(element, text, type) {
  element.textContent = text;
  element.className = `message ${type}`;
}

function setLoading(isLoading) {
  submitButton.disabled = isLoading;
  refreshButton.disabled = isLoading;
  cancelButton.disabled = isLoading;
}
