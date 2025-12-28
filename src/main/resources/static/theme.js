document.addEventListener("DOMContentLoaded", function () {
    console.log("theme.js loaded");

    const toggleBtn = document.getElementById("themeToggle");

    if (localStorage.getItem("theme") === "dark") {
        document.body.classList.add("dark");
        if (toggleBtn) toggleBtn.textContent = "☀️ Light Mode";
    }

    if (toggleBtn) {
        toggleBtn.addEventListener("click", function () {
            document.body.classList.toggle("dark");

            if (document.body.classList.contains("dark")) {
                localStorage.setItem("theme", "dark");
                toggleBtn.textContent = "☀️ Light Mode";
            } else {
                localStorage.setItem("theme", "light");
                toggleBtn.textContent = "🌙 Dark Mode";
            }
        });
    }
});

