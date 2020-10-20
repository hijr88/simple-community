'use strict'

let firstImageSrc;

// action이 profile일 경우 프로필 이미지 저장
if (document.querySelector('form').action.split('/').pop() === 'profile') {
    firstImageSrc = document.querySelector('#profile-image').src;
}

//닉네임 유효성 검사
function validateName(name, errText) {  //닉네임 input, 에러 input
    const regName = /^[0-9a-zA-Z가-힣]{1,8}$/; // 특수문자제외
    if (name.value.length === 0) {
        errText.textContent = '닉네임을 입력하세요.';
        name.focus();
        return false;
    }
    if (!regName.test(name.value)) {
        errText.textContent = '닉네임은 공백 특수문자를 제외한 1~8자입니다.';
        name.focus();
        return false;
    } else {
        errText.textContent = '';
    }
    return true;
}

function fileTrigger() {
    const fileButton = document.querySelector("#input-file");
    fileButton.click();
}

function changeNickName() {
    if (functionKey.includes(event.keyCode)) return;
    document.querySelector("#apply").classList.remove('disabled');
}

//이미지 파일 유효성 검사
function changeImageFile(fileButton) {
    const filePath = fileButton.value;
    if (filePath.length === 0) {
        // 원래 이미지로
        document.querySelector('#profile-image').src = firstImageSrc;
        return;
    }
    const file = fileButton.files[0];
    if (file.size > 10485760) {
        alert('최대 허용 파일 사이즈는 10mb 입니다.');
        return;
    }
    const acceptedImageTypes = ['image/gif', 'image/jpeg', 'image/png', 'image/bmp', 'image/tiff']; //허용가능한 파일 확장자
    if (!acceptedImageTypes.includes(file.type)) {
        alert('gif, jpeg, png, bmp, tiff 같은 이미지를 올려주세요.');
        return;
    }
    document.querySelector("#delete-button").classList.remove('disabled');  //삭제버튼
    document.querySelector("#isDelete").value = 'false';

    const thumbImg = document.querySelector("#profile-image");
    makeThumbnail(file, thumbImg, 160, 160);

    document.querySelector("#delete-button").classList.add('btn-outline-danger');
    document.querySelector("#delete-button").classList.remove('disabled', 'btn-outline-secondary');
    document.querySelector("#apply").classList.remove('disabled');
}

function deleteProfileImage(button) {
    if (button.classList.contains('disabled')) return;  //삭제버튼 비활성화일 경우 리턴

    document.querySelector("#isDelete").value = 'true';
    const img = document.querySelector("#profile-image");
    img.src = getRoot() + '/files/thumb/profile/anonymous/none?w=160&h=160';
    document.querySelector("#input-file").value = '';

    button.classList.remove('btn-outline-danger');
    button.classList.add('disabled', 'btn-outline-secondary');
    document.querySelector("#apply").classList.remove('disabled');
}

async function modifyMember(form) {
    if (form.querySelector('#apply').classList.contains('disabled')) return; //비활성화일 경우 리턴

    const type = form.action.split('/').pop();

    //프로필 수정
    if (type === 'profile') {
        //유효한 닉네임이 아닌 경우
        const name = document.querySelector("#nickname");
        const nameErr = document.querySelector(".error-name");
        if (!validateName(name, nameErr)) return;
    } else if (type === 'password') {
        const passwordArray = form.querySelectorAll("input[type=password]");
        const result = await matchPassword(passwordArray);
        if (!result) return;
    }

    form.submit();
}

//비밀번호 유효성 검사
function validatePassword(password) {
    return !(password.value.includes(' ') || password.value.length < 4 || password.value.length > 20);
}

//현재 비밀번호, 새로운 비밀번호, 새로운 비밀번호 일치하는지 확인
async function matchPassword(passwordArray) {

    for (let i = 0; i < 2; i++) {
        if (!validatePassword(passwordArray[i])) {
            passwordArray[i].value = '';
            passwordArray[i].focus();
            alert('비밀번호는 공백을 제외한 4~20자입니다.');
            return false;
        }
    }
    if (passwordArray[0].value === passwordArray[1].value) {
        alert('현재 비밀번호와 새 비밀번호가 일치합니다.');
        passwordArray[1].value = '';
        passwordArray[1].focus();
        return false;
    }
    if (passwordArray[1].value !== passwordArray[2].value) {
        alert('새 비밀번호 확인이 일치 하지 않습니다.');
        passwordArray[2].focus();
        return false;
    }

    const response = await fetch(getRoot() + `/api/members/match-password`,{
        method: 'post',
        headers: {'Content-Type' : 'text/plain'},
        body: passwordArray[0].value.trim()
    });
    if (!response.ok) {
        alert('Server Error');
        return;
    }
    const text = await response.text();

    if (text === '1') {
        return true;
    } else {
        alert('비밀번호를 정확하게 입력해 주세요.');
        return false;
    }
}

function deleteAddress() {
    const addressArray = document.querySelectorAll(".form-control");
    for (let address of addressArray)
        address.value = '';
    document.querySelector('#apply').classList.remove('disabled');
}

function changeAddress() {
    if (functionKey.includes(event.keyCode)) return;
    document.querySelector('#apply').classList.remove('disabled');
}

async function dropMember(form) {
    const passwordArray = document.querySelectorAll('input[type=password]');

    if (passwordArray[0].value.length === 0 || passwordArray[1].value.length === 0) {
        showAlert('info', '비밀번호를 입력하세요.', true);
        return;
    }

    if (passwordArray[0].value !== passwordArray[1].value) {
        showAlert('info', '비밀번호 확인이 일치하지 않습니다.', true);
        return;
    }

    const response = await fetch(getRoot() + `/api/members/match-password`,{
        method: 'post',
        headers: {'Content-Type' : 'text/plain'},
        body: passwordArray[0].value.trim()
    });
    if (!response.ok) {
        alert('Server Error');
    }
    const text = await response.text();

    if (text === '0') {
        showAlert('danger', '비밀번호가 일치하지 않습니다.', true);
    } else {
        if (confirm('정말 탈퇴하시겠습니까?')) {
            form.submit();
        }
    }
}