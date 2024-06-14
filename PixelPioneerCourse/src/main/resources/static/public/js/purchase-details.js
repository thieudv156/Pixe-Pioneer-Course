function toggleCreditCardDetails() {
    const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked').value;
    const creditCardDetails = document.getElementById("creditCardDetails");
    if (paymentMethod === "CREDIT_CARD") {
        creditCardDetails.style.display = "block";
    } else {
        creditCardDetails.style.display = "none";
    }
}

function showValidationError(elementId, message) {
    const errorElement = document.getElementById(elementId + "-error");
    errorElement.textContent = message;
    errorElement.style.display = "block";
}

function clearValidationErrors() {
    const errorElements = document.querySelectorAll(".validation-error");
    errorElements.forEach(element => {
        element.style.display = "none";
        element.textContent = "";
    });
}

function validateCreditCardDetails() {
    const cardNumber = document.getElementById("cardNumber").value;
    const expiration = document.getElementById("expiration").value;
    const cvv = document.getElementById("cvv").value;

    let valid = true;

    if (!/^[0-9]{16}$/.test(cardNumber)) {
        showValidationError("cardNumber", "Invalid card number. Please enter a valid 16-digit card number.");
        valid = false;
    }

    if (!/^(0[1-9]|1[0-2])\/\d{2}$/.test(expiration)) {
        showValidationError("expiration", "Invalid expiration date. Format should be MM/YY.");
        valid = false;
    }

    if (!/^[0-9]{3,4}$/.test(cvv)) {
        showValidationError("cvv", "Invalid CVV. Please enter a valid 3 or 4-digit CVV.");
        valid = false;
    }

    return valid;
}

function processPayment() {
    clearValidationErrors();

    const userId = document.getElementById("userId").value;
    const subscriptionType = document.getElementById("subscriptionType").value;
    const price = document.getElementById("price").value;
    const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked').value;

    const data = {
        userId: userId,
        subscriptionType: subscriptionType,
        price: price,
        paymentMethod: paymentMethod
    };

    if (paymentMethod === "CREDIT_CARD") {
        if (!validateCreditCardDetails()) {
            return; // Không gửi yêu cầu nếu thông tin thẻ tín dụng không hợp lệ
        }

        data.cardNumber = document.getElementById("cardNumber").value;
        data.expiration = document.getElementById("expiration").value;
        data.cvv = document.getElementById("cvv").value;
    }

    $.ajax({
        url: '/api/enrollments/process-payment',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (response) {
            if (paymentMethod === "PAYPAL") {
                window.location.href = response;
            } else {
                window.location.href = "/app/enrollments/payment-success";
            }
        },
        error: function (error) {
            document.getElementById("error").innerText = "Subscription failed: " + error.responseText;
        }
    });
}

document.addEventListener('DOMContentLoaded', function () {
    const paymentMethods = document.querySelectorAll('input[name="paymentMethod"]');
    paymentMethods.forEach(method => {
        method.addEventListener('change', toggleCreditCardDetails);
    });
    toggleCreditCardDetails();
});
