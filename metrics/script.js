document.addEventListener("DOMContentLoaded", () => {
    const dashboardContent = document.getElementById("dashboard-content");
    const loadingIndicator = document.getElementById("loading-indicator");
    const errorMessage = document.getElementById("error-message");

    const formatDate = (dateString) => {
        if (!dateString) return "N/A";
        const options = { year: "numeric", month: "short", day: "numeric", hour: "2-digit", minute: "2-digit" };
        return new Date(dateString).toLocaleString("en-US", options);
    };

    const renderDashboard = (data) => {
        loadingIndicator.classList.add("hidden");

        if (!data || data.length === 0) {
            dashboardContent.innerHTML = '<p class="text-center text-gray-600">No PR metrics data available.</p>';
            return;
        }

        const prsByWeek = data.reduce((acc, pr) => {
            const week = pr.week || "Unknown Week";
            if (!acc[week]) {
                acc[week] = [];
            }
            acc[week].push(pr);
            return acc;
        }, {});

        const sortedWeeks = Object.keys(prsByWeek).sort();

        dashboardContent.innerHTML = "";

        sortedWeeks.forEach((week) => {
            const prsInWeek = prsByWeek[week];
            const mergedCount = prsInWeek.filter((pr) => pr.status === "merged").length;
            const closedCount = prsInWeek.filter((pr) => pr.status === "closed").length;
            const pendingCount = prsInWeek.filter((pr) => pr.status === "pending_approval" || pr.status === "reopened").length;
            const approvedRequiredCount = prsInWeek.filter((pr) => pr.status === "approved_required").length;

            const weekGroup = document.createElement("div");
            weekGroup.classList.add("week-group");

            const weekSummary = document.createElement("div");
            weekSummary.classList.add("week-summary");

            weekSummary.innerHTML = `
                        <span>${week} (${prsInWeek.length} PRs)</span>
                        <svg class="arrow-icon w-6 h-6 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
                        </svg>
                    `;
            weekGroup.appendChild(weekSummary);

            const weekSummaryStatsBar = document.createElement("div");
            weekSummaryStatsBar.classList.add("week-summary-stats-bar");
            weekSummaryStatsBar.innerHTML = `
                        <span><span class="dot dot-merged"></span> Merged: ${mergedCount}</span>
                        <span><span class="dot dot-closed"></span> Closed: ${closedCount}</span>
                        <span><span class="dot dot-pending"></span> Pending: ${pendingCount}</span>
                        <span><span class="dot dot-approved"></span> Approved: ${approvedRequiredCount}</span>
                    `;
            weekGroup.appendChild(weekSummaryStatsBar);

            const weekContent = document.createElement("div");
            weekContent.classList.add("week-content");

            const sortedPrs = prsInWeek.sort((a, b) => a.pr_number - b.pr_number);

            sortedPrs.forEach((pr) => {
                const prCard = document.createElement("div");
                prCard.classList.add("pr-card", "grid", "grid-cols-1", "md:grid-cols-2", "lg:grid-cols-3", "gap-6", "items-start");

                const statusClass = `status-${pr.status ? pr.status.toLowerCase().replace(/[^a-z0-9]/g, "_") : "unknown"}`;

                prCard.innerHTML = `
                            <div class="col-span-full md:col-span-1">
                                <h3 class="text-lg font-semibold text-blue-700 mb-1">
                                    <a href="${pr.url}" target="_blank" class="hover:underline">PR #${pr.pr_number}: ${pr.title}</a>
                                </h3>
                                <p class="text-sm text-gray-600">Opened by: <a href="${pr.creator.url}" target="_blank" class="text-blue-500 hover:underline">${pr.creator.login}</a> on ${formatDate(pr.opened_at)}</p>
                                <span class="status-badge ${statusClass}">${pr.status || "Unknown"}</span>
                            </div>
                            <div>
                                <h4 class="font-medium text-gray-700 mb-1">Approvals (${pr.approvals ? pr.approvals.length : 0})</h4>
                                <ul class="list-disc list-inside text-sm text-gray-600">
                                    ${
                                        pr.approvals && pr.approvals.length > 0
                                            ? pr.approvals.map((a) => `<li><a href="${a.reviewer.url}" target="_blank" class="text-blue-500 hover:underline">${a.reviewer.login}</a> at ${formatDate(a.submitted_at)}</li>`).join("")
                                            : "<li>No approvals yet.</li>"
                                    }
                                </ul>
                            </div>
                            <div>
                                <h4 class="font-medium text-gray-700 mb-2">Metrics</h4>
                                <p class="text-sm text-gray-600 mb-2"><span class="font-semibold">First Approved:</span> ${formatDate(pr.first_approved_at)} (${
                    pr.time_to_first_approval_minutes !== null ? `${pr.time_to_first_approval_minutes.toFixed(0)} min` : "N/A"
                })</p>
                                <p class="text-sm text-gray-600 mb-2"><span class="font-semibold">Required Approvals Met:</span> ${formatDate(pr.required_approvals_met_at)} (${
                    pr.time_to_required_approvals_minutes !== null ? `${pr.time_to_required_approvals_minutes.toFixed(0)} min` : "N/A"
                })</p>
                                <p class="text-sm text-gray-600 mb-2"><span class="font-semibold">Closed At:</span> ${formatDate(pr.closed_at)}</p>
                                <p class="text-sm text-gray-600"><span class="font-semibold">Merged At:</span> ${formatDate(pr.merged_at)} ${
                    pr.merged_by ? `by <a href="${pr.merged_by.url}" target="_blank" class="text-blue-500 hover:underline">${pr.merged_by.login}</a>` : ""
                }</p>
                            </div>
                        `;
                weekContent.appendChild(prCard);
            });

            weekGroup.appendChild(weekContent);
            dashboardContent.appendChild(weekGroup);

            weekSummary.addEventListener("click", () => {
                weekContent.classList.toggle("expanded");
                const arrowIcon = weekSummary.querySelector(".arrow-icon");
                arrowIcon.classList.toggle("rotated");
            });
        });
    };

    fetch("pr_metrics.json")
        .then((response) => {
            if (!response.ok) {
                console.error(`HTTP error! status: ${response.status}. Using sample data.`);
                loadingIndicator.classList.add("hidden");
                errorMessage.classList.remove("hidden");
                return;
            }
            return response.json();
        })
        .then((data) => {
            renderDashboard(data);
        })
        .catch((error) => {
            console.error("Error fetching PR metrics:", error);
            loadingIndicator.classList.add("hidden");
            errorMessage.classList.remove("hidden");
        });
});
