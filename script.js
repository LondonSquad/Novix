document.addEventListener("DOMContentLoaded", () => {
    // --- DOM Elements ---
    const statusArea = document.getElementById('status-area');
    const contentArea = document.getElementById('content-area');
    const prMetricsContainer = document.getElementById("pr-metrics-container");
    const analyticsContainer = document.getElementById("analytics-content");
    const feedbackContainer = document.getElementById("feedback-content");
    const tabButtons = document.querySelectorAll(".tab-button");
    const tabContents = document.querySelectorAll(".tab-content");
    const filterButtons = document.querySelectorAll(".filter-btn");
    const searchInput = document.getElementById("pr-search");
    const themeToggleBtn = document.getElementById('theme-toggle-btn');
    const sunIcon = document.getElementById('theme-icon-sun');
    const moonIcon = document.getElementById('theme-icon-moon');

    // --- Chart instances & Icons ---
    let contributionsChart = null, reviewersChart = null;
    const ICONS = {
        warning: `<svg class="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"></path></svg>`,
        lightbulb: `<svg class="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z"></path></svg>`
    };

    // --- DARK MODE LOGIC ---
    const applyTheme = (theme) => {
        if (theme === 'dark') {
            document.documentElement.classList.add('dark');
            sunIcon.classList.remove('hidden');
            moonIcon.classList.add('hidden');
        } else {
            document.documentElement.classList.remove('dark');
            sunIcon.classList.add('hidden');
            moonIcon.classList.remove('hidden');
        }
        localStorage.setItem('theme', theme);
        
        // Update charts if they exist
        [contributionsChart, reviewersChart].forEach(chart => {
            if (chart) {
                const isDark = theme === 'dark';
                const gridColor = isDark ? 'rgba(107, 114, 128, 0.3)' : 'rgba(209, 213, 219, 0.5)';
                const textColor = isDark ? '#d1d5db' : '#4b5563';
                chart.options.scales.x.grid.color = gridColor;
                chart.options.scales.y.grid.color = gridColor;
                chart.options.scales.x.ticks.color = textColor;
                chart.options.scales.y.ticks.color = textColor;
                chart.options.plugins.legend.labels.color = textColor;
                chart.update();
            }
        });
    };

    themeToggleBtn.addEventListener('click', () => {
        const newTheme = document.documentElement.classList.contains('dark') ? 'light' : 'dark';
        applyTheme(newTheme);
    });

    // --- UTILITY FUNCTIONS ---
    const formatDate = (d) => { if (!d) return "N/A"; return new Date(d).toLocaleString("en-US", { month: "short", day: "numeric", hour: "2-digit", minute: "2-digit" }); };
    const formatDuration = (m) => {
        if (m === null || isNaN(m) || m < 0) return "N/A";
        if (m < 1) return `${(m * 60).toFixed(0)}s`;
        if (m < 60) return `${m.toFixed(0)}m`;
        if (m < 1440) return `${(m / 60).toFixed(1)}h`;
        return `${(m / 1440).toFixed(1)}d`;
    };

    // --- TAB SWITCHING ---
    tabButtons.forEach(button => {
        button.addEventListener("click", () => {
            tabButtons.forEach(btn => btn.classList.remove("active"));
            button.classList.add("active");
            tabContents.forEach(content => content.classList.add("hidden"));
            document.getElementById(button.dataset.tab + "-content").classList.remove("hidden");
        });
    });

    // --- RENDER PR METRICS (MODERN DESIGN) ---
    const renderPrMetrics = (data) => {
        const sortedPrs = data.sort((a, b) => new Date(b.opened_at) - new Date(a.opened_at));
        let prListHtml = '';

        sortedPrs.forEach(pr => {
            let simpleStatus = pr.status;
            if (simpleStatus === 'approved' || simpleStatus === 'reopened') simpleStatus = 'pending';
            
            const sortedApprovals = (pr.approvals || []).sort((a,b) => new Date(a.submitted_at) - new Date(b.submitted_at));
            const firstApproval = sortedApprovals[0];
            const secondApproval = sortedApprovals[1];
            
            const timeBetweenApprovals = firstApproval && secondApproval 
                ? (new Date(secondApproval.submitted_at) - new Date(firstApproval.submitted_at)) / 60000 
                : null;

            const approversHtml = (sortedApprovals.length > 0)
                ? sortedApprovals.map(a => `
                    <div class="flex items-center gap-2">
                        <img src="https://github.com/${a.reviewer.login}.png" alt="${a.reviewer.login}" class="avatar rounded-full"/>
                        <span class="font-medium text-sm">${a.reviewer.login}</span>
                    </div>`).join('')
                : '<span class="text-sm text-secondary">No approvals yet.</span>';
            
            let timelineItems = [{ status: 'created', date: pr.opened_at, text: 'Created' }];
            if (firstApproval) timelineItems.push({ status: 'approved', date: firstApproval.submitted_at, text: '1st Approval' });
            if (secondApproval) timelineItems.push({ status: 'approved', date: secondApproval.submitted_at, text: '2nd Approval' });
            if (pr.merged_at) timelineItems.push({ status: 'merged', date: pr.merged_at, text: 'Merged' });
            timelineItems.sort((a,b) => new Date(a.date) - new Date(b.date));

            prListHtml += `
                <div class="pr-row" data-status="${simpleStatus}" data-text="${pr.title.toLowerCase()} #${pr.pr_number}">
                    <div class="pr-row-main">
                        <div class="pr-info-cell">
                            <div class="pr-title"><a href="${pr.url}" target="_blank">#${pr.pr_number} ${pr.title}</a></div>
                            <div class="pr-meta">Opened on ${formatDate(pr.opened_at)}</div>
                        </div>
                        <div class="pr-author-cell">
                            <img src="https://github.com/${pr.creator.login}.png" alt="${pr.creator.login}" class="avatar"/>
                            <span class="text-secondary">${pr.creator.login}</span>
                        </div>
                        <div class="pr-status-badge status-${simpleStatus}">${pr.status}</div>
                        <div class="stats-item">
                            <span class="added">+${pr.additions || 0}</span>
                            <span class="removed">-${pr.deletions || 0}</span>
                        </div>
                        <div class="pr-details-toggle">
                            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path></svg>
                        </div>
                    </div>
                    <div class="pr-row-details">
                        <div class="details-grid">
                            <div class="detail-section">
                                <h4>Timeline</h4>
                                <div class="relative pt-2">
                                    ${timelineItems.map(item => `
                                        <div class="timeline-item" data-status="${item.status}">
                                            <div class="timeline-line"></div>
                                            <div class="timeline-marker"></div>
                                            <strong>${item.text}</strong>
                                            <div class="text-sm text-secondary">${formatDate(item.date)}</div>
                                        </div>
                                    `).join('')}
                                </div>
                            </div>
                            <div class="detail-section">
                                <h4>Approvers</h4>
                                <div class="approvals-list space-y-2">${approversHtml}</div>
                            </div>
                            <div class="detail-section">
                                <h4>Key Metrics</h4>
                                <div class="space-y-3 text-sm">
                                    <div class="metric-item">
                                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm.75-13a.75.75 0 00-1.5 0v5c0 .414.336.75.75.75h4a.75.75 0 000-1.5h-3.25V5z" clip-rule="evenodd" /></svg>
                                        <div><strong>Time to 1st Approval:</strong><br>${formatDuration(pr.time_to_first_approval_minutes)}</div>
                                    </div>
                                    <div class="metric-item">
                                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor"><path d="M10 2a.75.75 0 01.75.75v1.5a.75.75 0 01-1.5 0v-1.5A.75.75 0 0110 2zM10 15a.75.75 0 01.75.75v1.5a.75.75 0 01-1.5 0v-1.5A.75.75 0 0110 15zM10 7a3 3 0 100 6 3 3 0 000-6z" /></svg>
                                        <div><strong>Time 1st → 2nd Approval:</strong><br>${formatDuration(timeBetweenApprovals)}</div>
                                    </div>
                                    <div class="metric-item">
                                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.857-9.809a.75.75 0 00-1.214-.882l-3.483 4.79-1.88-1.88a.75.75 0 10-1.06 1.061l2.5 2.5a.75.75 0 001.137-.089l4-5.5z" clip-rule="evenodd" /></svg>
                                        <div><strong>Time to Merge:</strong><br>${formatDuration(pr.merged_at ? (new Date(pr.merged_at) - new Date(pr.opened_at)) / 60000 : null)}</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>`;
        });
        
        prMetricsContainer.innerHTML = `<div class="pr-table-wrapper">
            <div class="pr-table-header"><span>Pull Request</span><span>Author</span><span>Status</span><span>Diff</span><span></span></div>
            ${prListHtml}</div>`;

        prMetricsContainer.querySelectorAll('.pr-row-main').forEach(row => {
            row.addEventListener('click', () => row.parentElement.classList.toggle('details-expanded'));
        });
        
        const applyFilters = () => {
            const activeStatus = filterButtonsContainer.querySelector('.filter-btn.active').dataset.status;
            const searchText = searchInput.value.toLowerCase();
            prMetricsContainer.querySelectorAll('.pr-row').forEach(item => {
                const isStatusMatch = activeStatus === 'all' || item.dataset.status === activeStatus;
                const isTextMatch = item.dataset.text.includes(searchText);
                item.style.display = isStatusMatch && isTextMatch ? 'flex' : 'none';
            });
        };
        const filterButtonsContainer = document.querySelector('.filter-buttons');
        filterButtonsContainer.addEventListener('click', (e) => {
            if (e.target.classList.contains('filter-btn')) {
                filterButtonsContainer.querySelector('.active').classList.remove('active');
                e.target.classList.add('active');
                applyFilters();
            }
        });
        searchInput.addEventListener('keyup', applyFilters);
    };

    // --- RENDER ANALYTICS & FEEDBACK ---
    const renderAnalytics = (data) => {
        analyticsContainer.innerHTML = `
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                <div class="kpi-card"><div class="kpi-icon bg-purple-500"><svg viewBox="0 0 24 24" fill="currentColor"><path d="M12 2C6.477 2 2 6.477 2 12C2 17.523 6.477 22 12 22C17.523 22 22 17.523 22 12C22 6.477 17.523 2 12 2ZM15.28 17L11.25 14.5L7.22 17L8.25 12.3L4.5 9.25L9.25 8.88L11.25 4.5L13.25 8.88L18 9.25L14.25 12.3L15.28 17Z"></path></svg></div><div><div id="kpi-total-merged" class="kpi-value">-</div><div class="kpi-label">Total Merged PRs</div></div></div>
                <div class="kpi-card"><div class="kpi-icon bg-sky-500"><svg viewBox="0 0 24 24" fill="currentColor"><path d="M12 2C17.52 2 22 6.48 22 12C22 17.52 17.52 22 12 22C6.48 22 2 17.52 2 12C2 6.48 6.48 2 12 2ZM12 4C7.58 4 4 7.58 4 12C4 16.42 7.58 20 12 20C16.42 20 20 16.42 20 12C20 7.58 16.42 4 12 4ZM12.5 7V12.25L16.21 14.33L15.53 15.25L11 12.5V7H12.5Z"></path></svg></div><div><div id="kpi-avg-approval-time" class="kpi-value">-</div><div class="kpi-label">Avg. Time to 1st Approval</div></div></div>
                <div class="kpi-card"><div class="kpi-icon bg-green-500"><svg viewBox="0 0 24 24" fill="currentColor"><path d="M11.99 2C6.47 2 2 6.48 2 12C2 17.52 6.47 22 11.99 22C17.52 22 22 17.52 22 12C22 6.48 17.52 2 11.99 2ZM16.24 7.76L10 14.01l-2.24-2.25a.996.996 0 10-1.41 1.41l3 3c.39.39 1.02.39 1.41 0l7-7a.996.996 0 000-1.41c-.39-.39-1.03-.39-1.42 0z"></path></svg></div><div><div id="kpi-avg-lifespan" class="kpi-value">-</div><div class="kpi-label">Avg. PR Lifespan</div></div></div>
                <div id="hot-streak-card" class="kpi-card spotlight-card"></div><div id="fastest-reviewer-card" class="kpi-card spotlight-card"></div>
            </div>
            <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
                <div class="panel"><h3 class="panel-header panel-title">Contribution Leaderboard</h3><div class="panel-body chart-container"><canvas id="contributionsChart"></canvas></div></div>
                <div class="panel"><h3 class="panel-header panel-title">Review Load Distribution</h3><div class="panel-body chart-container"><canvas id="reviewersChart"></canvas></div></div>
                <div class="panel lg:col-span-2"><h3 class="panel-header panel-title">Top Collaboration Pairs</h3><div class="panel-body"><ul id="collaboration-list" class="collaboration-list"></ul></div></div>
            </div>`;

        const mergedPRs = data.filter(pr => pr.status === 'merged');
        document.getElementById('kpi-total-merged').textContent = mergedPRs.length;
        const approvalTimes = data.map(pr => pr.time_to_first_approval_minutes).filter(t => t !== null && t >= 0);
        document.getElementById('kpi-avg-approval-time').textContent = formatDuration(approvalTimes.length ? approvalTimes.reduce((a, b) => a + b, 0) / approvalTimes.length : 0);
        const lifespans = mergedPRs.map(pr => (new Date(pr.merged_at) - new Date(pr.opened_at)) / 60000).filter(t => t > 0);
        document.getElementById('kpi-avg-lifespan').textContent = formatDuration(lifespans.length ? lifespans.reduce((a, b) => a + b, 0) / lifespans.length : 0);
        
        const sevenDaysAgo = new Date(Date.now() - 7 * 24 * 60 * 60 * 1000);
        const recentPRs = data.filter(pr => new Date(pr.opened_at) > sevenDaysAgo);
        const recentActivity = recentPRs.reduce((acc, pr) => {
            acc[pr.creator.login] = (acc[pr.creator.login] || 0) + 1;
            if (pr.merged_by) acc[pr.merged_by.login] = (acc[pr.merged_by.login] || 0) + 1;
            return acc;
        }, {});
        const hotStreakUser = Object.entries(recentActivity).sort((a,b) => b[1] - a[1])[0];
        document.getElementById('hot-streak-card').innerHTML = hotStreakUser ? `<div class="kpi-icon bg-orange-500"><svg viewBox="0 0 24 24" fill="currentColor"><path d="M11.163 2.006a2.008 2.008 0 0 1 1.674 0 23.33 23.33 0 0 0 3.332.943c.42.115.823.383 1.12.756.298.373.473.85.49 1.349.02.535-.118 1.063-.393 1.503-.276.44-.678.78-1.144 1.004a25.34 25.34 0 0 1-4.242 1.445c-.486.136-1.003.136-1.489 0a25.34 25.34 0 0 1-4.242-1.445c-.466-.223-.868-.564-1.144-1.004-.275-.44-.413-1.028-.393-1.563.017-.499.192-.976.49-1.349.297-.373.7-.64 1.12-.756a23.33 23.33 0 0 0 3.332-.943ZM12 11a3 3 0 0 1 3 3c0 1.25-.75 3.5-3 5.25-2.25-1.75-3-4-3-5.25a3 3 0 0 1 3-3Z"></path></svg></div><div class="overflow-hidden"><div class="kpi-value"><img src="https://github.com/${hotStreakUser[0]}.png" class="avatar"/> <span class="truncate">${hotStreakUser[0]}</span></div><div class="kpi-label">Hot Streak (${hotStreakUser[1]} activities in 7 days)</div></div>` : `<div class="p-4 text-center">No recent activity.</div>`;

        const recentReviews = recentPRs.reduce((acc, pr) => {
            if(pr.time_to_first_approval_minutes !== null) {
                const reviewer = (pr.approvals || []).sort((a,b) => new Date(a.submitted_at) - new Date(b.submitted_at))[0]?.reviewer.login;
                if(reviewer) {
                    (acc[reviewer] = acc[reviewer] || []).push(pr.time_to_first_approval_minutes);
                }
            }
            return acc;
        }, {});
        const fastestReviewerData = Object.entries(recentReviews).map(([user, times]) => ({user, avg: times.reduce((a,b)=>a+b,0)/times.length})).sort((a,b) => a.avg - b.avg)[0];
        document.getElementById('fastest-reviewer-card').innerHTML = fastestReviewerData ? `<div class="kpi-icon bg-teal-500"><svg viewBox="0 0 24 24" fill="currentColor"><path fill-rule="evenodd" d="M12.963 2.286a.75.75 0 0 0-1.071 1.052A32.11 32.11 0 0 1 12 11.897a.75.75 0 0 1-1.5 0 33.61 33.61 0 0 0-1.433-8.56A.75.75 0 0 0 7.963 2.286 33.337 33.337 0 0 0 3 11.25a.75.75 0 0 0 1.5 0c0-1.88.243-3.727.712-5.522A31.838 31.838 0 0 1 12 21.75c2.784 0 5.488-.344 8.088-.98a.75.75 0 0 0-.21-1.474c-1.92.44-3.926.654-5.978.654-3.776 0-7.398-1.026-10.68-2.852A31.838 31.838 0 0 1 2.288 5.728a.75.75 0 0 0-1.052-1.07C.4 5.478 0 6.474 0 7.5c0 1.573.493 3.091 1.369 4.417.876 1.326 2.087 2.41 3.533 3.193a.75.75 0 0 0 .937-.587c.106-.49.227-.978.362-1.463a.75.75 0 0 0-.584-.863 15.65 15.65 0 0 1-1.123-.424c.2-.55.43-1.096.683-1.636.76 1.11 1.835 1.99 3.167 2.61a.75.75 0 0 0 .93-.593c.08-.39.15-.783.21-1.179a.75.75 0 0 0-.55-.838c-.375-.123-.74-.25-1.091-.383.612-.953 1.08-2.01 1.38-3.155a.75.75 0 0 0-.68-.82 14.89 14.89 0 0 1-1.28-.21C9.69 6.27 11.27 3.5 12.963 2.286Z" clip-rule="evenodd"></path></svg></div><div class="overflow-hidden"><div class="kpi-value"><img src="https://github.com/${fastestReviewerData.user}.png" class="avatar"/> <span class="truncate">${fastestReviewerData.user}</span></div><div class="kpi-label">Fastest Reviewer (${formatDuration(fastestReviewerData.avg)} avg)</div></div>` : `<div class="p-4 text-center">No recent reviews.</div>`;

        // --- CORRECTED: Contribution Chart Logic ---
        const contributions = data.reduce((acc, pr) => {
            const creator = pr.creator.login;
            acc[creator] = acc[creator] || { o: 0, m: 0 };
            acc[creator].o++;

            if (pr.merged_by) {
                const merger = pr.merged_by.login;
                acc[merger] = acc[merger] || { o: 0, m: 0 };
                acc[merger].m++;
            }
            return acc;
        }, {});
        
        const chartLabels = Object.keys(contributions).sort();
        const openedData = chartLabels.map(label => contributions[label].o);
        const mergedData = chartLabels.map(label => contributions[label].m);
        // --- END CORRECTION ---

        const reviewers = data.flatMap(pr => pr.approvals || []).reduce((acc, a) => {(acc[a.reviewer.login]=(acc[a.reviewer.login]||0)+1); return acc;}, {});
        
        const collaborationSummary = data.reduce((acc, pr) => {
            const creator = pr.creator.login;
            (pr.approvals || []).forEach(approval => {
                const reviewer = approval.reviewer.login;
                if (creator === reviewer) return;
                if (!acc[reviewer]) {
                    acc[reviewer] = { totalReviews: 0, collaborators: new Set() };
                }
                acc[reviewer].totalReviews++;
                acc[reviewer].collaborators.add(creator);
            });
            return acc;
        }, {});

        const sortedCollaborators = Object.entries(collaborationSummary)
            .map(([reviewer, data]) => ({ reviewer, ...data }))
            .sort((a, b) => b.totalReviews - a.totalReviews)
            .slice(0, 10);

        document.getElementById('collaboration-list').innerHTML = sortedCollaborators.map(item => {
            const collaboratorAvatars = Array.from(item.collaborators).slice(0, 5).map(c => 
                `<a href="https://github.com/${c}" target="_blank" title="${c}"><img src="https://github.com/${c}.png" class="collaborator-avatar"></a>`
            ).join('');

            return `<li>
                        <div class="reviewer-summary">
                            <a href="https://github.com/${item.reviewer}" target="_blank">
                                <img src="https://github.com/${item.reviewer}.png" class="avatar"/>
                                <span>${item.reviewer}</span>
                            </a>
                            <span class="text-secondary">reviewed</span>
                            <div class="collaborator-avatar-stack">${collaboratorAvatars}</div>
                        </div>
                        <span class="collaboration-count">${item.totalReviews} total reviews</span>
                    </li>`;
        }).join('') || '<li>No collaboration data available.</li>';
        
        const getOrCreateTooltip = (chart) => {
            let el = document.querySelector('.chart-tooltip');
            if (!el) { el = document.createElement('div'); el.className = 'chart-tooltip'; document.body.appendChild(el); }
            return el;
        };
        const externalTooltipHandler = (context) => {
            const { chart, tooltip } = context;
            const el = getOrCreateTooltip(chart);
            if (tooltip.opacity === 0) { el.style.opacity = 0; return; }
            if(tooltip.body) {
                const user = tooltip.dataPoints[0].label;
                el.innerHTML = `<div class="chart-tooltip-header"><img src="https://github.com/${user}.png" class="avatar"/><strong>${user}</strong></div><div class="chart-tooltip-body">${tooltip.dataPoints.map(p => `<div>${p.dataset.label}: ${p.raw}</div>`).join('')}</div>`;
            }
            const pos = chart.canvas.getBoundingClientRect();
            el.style.opacity = 1;
            el.style.left = pos.left + window.scrollX + tooltip.caretX + 'px';
            el.style.top = pos.top + window.scrollY + tooltip.caretY + 'px';
        };

        if (contributionsChart) contributionsChart.destroy();
        contributionsChart = new Chart(document.getElementById('contributionsChart').getContext('2d'), { type: 'bar', data: { labels: chartLabels, datasets: [ { label: 'Opened', data: openedData, backgroundColor: 'rgba(79, 70, 229, 0.7)'}, { label: 'Merged', data: mergedData, backgroundColor: 'rgba(16, 185, 129, 0.7)'} ] }, options: { responsive: true, maintainAspectRatio: false, scales: { x: { stacked: true }, y: { stacked: true, beginAtZero: true } }, plugins: { legend: { position: 'bottom' }, tooltip: { enabled: false, external: externalTooltipHandler }}}});
        
        const sortedReviewers = Object.entries(reviewers).sort((a, b) => b[1] - a[1]);
        if (reviewersChart) reviewersChart.destroy();
        reviewersChart = new Chart(document.getElementById('reviewersChart').getContext('2d'), { type: 'bar', data: { labels: sortedReviewers.map(r => r[0]), datasets: [{ label: 'Approvals Given', data: sortedReviewers.map(r => r[1]), backgroundColor: 'rgba(2, 132, 199, 0.7)' }] }, options: { indexAxis: 'y', responsive: true, maintainAspectRatio: false, plugins: { legend: { display: false }, tooltip: { enabled: false, external: externalTooltipHandler }}}});
        
        applyTheme(localStorage.getItem('theme') || 'light');
    };

    const renderFeedback = (data) => {
        feedbackContainer.innerHTML = data.improvement_topics.map(topic => `
            <div class="panel feedback-topic-card">
                <h2 class="panel-header panel-title">${topic.topic_title}</h2>
                <div class="content-pair">
                    <div class="problem-section feedback-section">
                        <h3>${ICONS.warning} ${topic.problem_section.title}</h3>
                        <ul>${topic.problem_section.points.map(point => `<li>${point}</li>`).join('')}</ul>
                    </div>
                    ${topic.solution_section ? `<div class="solution-section feedback-section"><h3>${ICONS.lightbulb} ${topic.solution_section.title}</h3><ul>${topic.solution_section.points.map(point => `<li>${point}</li>`).join('')}</ul></div>` : ''}
                </div>
            </div>`).join('') + `<div class="feedback-summary">${data.summary}</div>`;
    };
    
    // --- MAIN DATA FETCHING & INITIALIZATION ---
    const main = async () => {
        const savedTheme = localStorage.getItem('theme') || (window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light');
        applyTheme(savedTheme);

        try {
            const [prResponse, feedbackResponse] = await Promise.all([
                fetch("https://raw.githubusercontent.com/LondonSquad/Novix/refs/heads/metrics/pr_metrics.json").then(res => res.ok ? res.json() : Promise.reject(res)),
                fetch("https://raw.githubusercontent.com/LondonSquad/Novix/refs/heads/metrics/feedbacks.json").then(res => res.ok ? res.json() : Promise.reject(res))
            ]);
            statusArea.style.display = 'none';
            contentArea.classList.remove("hidden");
            
            renderPrMetrics(prResponse);
            renderAnalytics(prResponse);
            renderFeedback(feedbackResponse);

        } catch (error) {
            console.error("Error fetching dashboard data:", error);
            statusArea.innerHTML = `<div class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4" role="alert"><p class="font-bold">Loading Failed</p><p>Could not fetch required data. Please check the console and ensure all files are accessible.</p></div>`;
        }
    };

    main();
});