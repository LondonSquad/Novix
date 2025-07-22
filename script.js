document.addEventListener("DOMContentLoaded", () => {
    // --- DOM Elements ---
    const statusArea = document.getElementById('status-area');
    const contentArea = document.getElementById('content-area');
    const prMetricsContainer = document.getElementById("pr-metrics-container");
    const analyticsContainer = document.getElementById("analytics-content");
    const ratingsContainer = document.getElementById("ratings-content");
    const feedbackContainer = document.getElementById("feedback-content");
    const tabButtons = document.querySelectorAll(".tab-button");
    const tabContents = document.querySelectorAll(".tab-content");
    const filterButtons = document.querySelectorAll(".filter-btn");
    const searchInput = document.getElementById("pr-search");
    const authorFilter = document.getElementById("author-filter");
    const analyticsWeekFilter = document.getElementById("analytics-week-filter");
    const themeToggleBtn = document.getElementById('theme-toggle-btn');
    const sunIcon = document.getElementById('theme-icon-sun');
    const moonIcon = document.getElementById('theme-icon-moon');
    const langSwitcher = document.querySelector('.lang-switcher');

    let allPrData = [];
    let allRatingsData = []; // Store ratings data globally
    let sortState = {
        key: 'title',
        direction: 'des'
    };

    // --- TRANSLATIONS ---
    const translations = {
        en: {
            // Intro Note
            introTitle: "A Note on Feedback:",
            introBody: "This evaluation reflects the perspective of your team members. We may not always see our own shortcomings, so it's a gift when they are kindly pointed out by teammates who wish us well and want to see us grow.",
            // Main Panels
            overallPerformance: "Overall Performance",
            fromLastEval: "from last evaluation",
            firstEval: "First evaluation",
            tagline: `"Growth is a journey, not a race."`,
            categoryBreakdown: "Category Breakdown",
            peerComments: "Peer Comments",
            noPeerComments: "No peer comments submitted.",
            personalNotes: "Personal Notes",
            noPersonalNotes: "No personal notes recorded.",
            performanceOverTime: "Performance Over Time",
            suggestionBox: "Suggestion Box",
            focusArea: "Focus Area",
            avgScore: "Avg. Score",
            // Skills
            respect: "Respect",
            teamwork: "Teamwork",
            listening: "Listening",
            flexibility: "Flexibility",
            encouragement: "Encouragement",
            communication: "Communication",
            constructive_feedback: "Constructive Feedback",
            technical: "Technical",
            // Tips
            tip_respect: "Try to actively acknowledge teammates' ideas before sharing your own to show they've been heard.",
            tip_teamwork: "Offer to help a teammate with a task this week, even if it's just for a few minutes.",
            tip_listening: "Practice active listening by paraphrasing what someone says to confirm your understanding before you respond.",
            tip_flexibility: "In a team environment, it's important to stay flexible and open to colleagues' opinions and feedback, even if they differ from our own. Listening with an open mind and engaging in calm discussions build trust and strengthen team spirit. Embracing constructive criticism helps us grow both personally and professionally.",
            tip_encouragement: "Make it a goal to give a specific, sincere compliment to at least two different teammates this week.",
            tip_communication: "Before sending a message, re-read it from the receiver's perspective. Is the key point clear?",
            tip_constructive_feedback: "When giving feedback, try the 'praise-critique-praise' sandwich method to keep it positive and actionable.",
            tip_technical: "Put your knowledge into practice, practical experience with debugging, UI/UX, and architecture teaches more than tutorials alone."
        },
        ar: {
            // Intro Note
            introTitle: "ملاحظة حول التقييم:",
            introBody: "يعكس هذا التقييم وجهة نظر أعضاء فريقك. قد لا نتمكن دائمًا من رؤية نقاط ضعفنا، لذا فإن الإشارة إليها بلطف من قبل من يرغبون في نموّنا تُعدّ هدية ثمينة.",
            // Main Panels
            overallPerformance: "الأداء العام",
            fromLastEval: "من التقييم السابق",
            firstEval: "أول تقييم",
            tagline: `"النمو رحلة وليس بسباق."`,
            categoryBreakdown: "تفصيل حسب الفئة",
            peerComments: "تعليقات الزملاء",
            noPeerComments: "لم يتم تقديم تعليقات من الزملاء.",
            personalNotes: "ملاحظات شخصية",
            noPersonalNotes: "لا توجد ملاحظات شخصية مسجّلة.",
            performanceOverTime: "الأداء على مدار الوقت",
            suggestionBox: "صندوق الاقتراحات",
            focusArea: "مجال التركيز",
            avgScore: "متوسط التقييم",
            // Skills
            respect: "الاحترام",
            teamwork: "العمل الجماعي",
            listening: "الإنصات",
            flexibility: "المرونة",
            encouragement: "التشجيع",
            communication: "التواصل",
            constructive_feedback: "النقد البناء",
            technical: "الجانب التقني",
            // Tips
            tip_respect: "حاول إظهار تقديرك لأفكار زملائك قبل مشاركة أفكارك الخاصة، لتبيّن أنك استمعت إليهم.",
            tip_teamwork: "اعرض المساعدة على زميل في مهمة هذا الأسبوع، حتى لو كان ذلك لبضع دقائق فقط.",
            tip_listening: "مارس مهارة الإنصات النشط بإعادة صياغة ما قيل لتأكيد فهمك قبل الرد.",
            tip_flexibility: "في بيئة العمل الجماعي، من المهم أن نتحلى بالمرونة ونُظهر تقبّلًا لآراء وملاحظات الزملاء حتى وإن اختلفت مع وجهة نظرنا. الاستماع بانفتاح والنقاش بهدوء يعززان الثقة ويقويان روح الفريق، كما أن تقبّل النقد البنّاء يساعدنا على التطور الشخصي والمهني.",
            tip_encouragement: "اجعل هدفك تقديم مجاملة محددة وصادقة لزميلين مختلفين على الأقل هذا الأسبوع.",
            tip_communication: "قبل إرسال أي رسالة، اقرأها من منظور المتلقي. هل الفكرة الأساسية واضحة؟",
            tip_constructive_feedback: "عند تقديم النقد، جرب طريقة 'الثناء - النقد - الثناء' للحفاظ على إيجابية وقابلية التنفيذ.",
            tip_technical: "ضع معرفتك موضع التطبيق، فالتجربة العملية في تصحيح الأخطاء وتصميم الواجهة وتجهيز البنية التحتية تُعلّم أكثر بكثير من الدروس النظرية وحدها."
        }
    };

    // --- Chart instances ---
    let contributionsChart = null,
        reviewersChart = null,
        mergeProcessHealthChart = null;
    let progressCharts = [];
    let radarCharts = [];

    const ICONS = {
        safe: `<svg class="icon" fill="currentColor" viewBox="0 0 20 20"><path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"></path></svg>`,
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

        const isDark = theme === 'dark';
        const gridColor = isDark ? 'rgba(107, 114, 128, 0.3)' : 'rgba(209, 213, 219, 0.5)';
        const textColor = isDark ? '#d1d5db' : '#4b5563';

        const allCharts = [
            contributionsChart,
            reviewersChart,
            mergeProcessHealthChart,
            ...progressCharts,
            ...radarCharts
        ];

        allCharts.forEach(chart => {
            if (chart) {
                try {
                    const scales = chart.options.scales;
                    if (scales) {
                        if (scales.x && scales.y) {
                            scales.x.grid.color = gridColor;
                            scales.y.grid.color = gridColor;
                            scales.x.ticks.color = textColor;
                            scales.y.ticks.color = textColor;
                        }
                        if (scales.r) {
                            scales.r.pointLabels.color = textColor;
                            scales.r.angleLines.color = gridColor;
                            scales.r.grid.color = gridColor;
                        }
                    }
                    if (chart.options.plugins.legend) {
                        chart.options.plugins.legend.labels.color = textColor;
                    }
                    chart.update();
                } catch (e) {
                    console.error("Failed to update theme for a chart:", e);
                }
            }
        });
    };

    themeToggleBtn.addEventListener('click', () => {
        const newTheme = document.documentElement.classList.contains('dark') ? 'light' : 'dark';
        applyTheme(newTheme);
    });

 // --- LANGUAGE SWITCHER LOGIC ---
    const setLanguage = (lang) => {
        if (lang !== 'en' && lang !== 'ar') lang = 'en'; // Default to English

        document.documentElement.lang = lang;
        ratingsContainer.dir = lang === 'ar' ? 'rtl' : 'ltr';

        langSwitcher.querySelectorAll('.lang-btn').forEach(btn => {
            btn.classList.toggle('active', btn.dataset.lang === lang);
        });

        // Rerender the ratings tab with the new language
        if(allRatingsData.length > 0) {
            renderRatings(allRatingsData, lang);
        }

        localStorage.setItem('language', lang);
    };

    langSwitcher.addEventListener('click', (e) => {
        const btn = e.target.closest('.lang-btn');
        if (btn) {
            setLanguage(btn.dataset.lang);
        }
    });

    // --- UTILITY FUNCTIONS ---
    const formatDate = (d) => {
        if (!d) return "N/A";
        return new Date(d).toLocaleString("en-US", {
            month: "short",
            day: "numeric",
            hour: "2-digit",
            minute: "2-digit"
        });
    };
    const formatDuration = (m) => {
        if (m === null || isNaN(m) || m < 0) return "N/A";
        if (m < 1) return `${(m * 60).toFixed(0)}s`;
        if (m < 60) return `${m.toFixed(0)}m`;
        if (m < 1440) return `${(m / 60).toFixed(1)}h`;
        return `${(m / 1440).toFixed(1)}d`;
    };

    const getWeekStartDate = (dateStr) => {
        const prDate = new Date(dateStr);
        const weekStart = new Date(prDate);
        let daysToSubtract = (prDate.getDay() + 1) % 7;
        weekStart.setDate(prDate.getDate() - daysToSubtract);
        weekStart.setHours(19, 0, 0, 0);

        if (prDate < weekStart) {
            weekStart.setDate(weekStart.getDate() - 7);
        }
        return weekStart;
    };

    const formatWeekHeader = (startDate) => {
        const endDate = new Date(startDate);
        endDate.setDate(startDate.getDate() + 7);
        endDate.setMilliseconds(endDate.getMilliseconds() - 1);

        const startMonth = startDate.toLocaleString('en-US', {
            month: 'short'
        });
        const endMonth = endDate.toLocaleString('en-US', {
            month: 'short'
        });

        let datePart;
        if (startMonth === endMonth) {
            datePart = `${startMonth} ${startDate.getDate()} - ${endDate.getDate()}`;
        } else {
            datePart = `${startMonth} ${startDate.getDate()} - ${endMonth} ${endDate.getDate()}`;
        }
        return `Week of ${datePart}, ${startDate.getFullYear()}`;
    };

    const getRelativeWeekName = (index, baseWeekNumber = 16) => {
        if (index === 0) {
            return "Current Week";
        }
        const weekNum = baseWeekNumber - (index - 1);
        return `Week ${weekNum}`;
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

    const generatePrListHtml = (data) => {
        return data.map(pr => {
            let filterStatus = pr.status;
            if (pr.status === 'approved' || pr.status === 'reopened') filterStatus = 'pending';

            const sortedApprovals = (pr.approvals || []).sort((a, b) => new Date(a.submitted_at) - new Date(b.submitted_at));
            const firstApproval = sortedApprovals[0];
            const secondApproval = sortedApprovals[1];
            const timeBetweenApprovals = firstApproval && secondApproval ? (new Date(secondApproval.submitted_at) - new Date(firstApproval.submitted_at)) / 60000 : null;
            const approversHtml = (sortedApprovals.length > 0) ?
                sortedApprovals.map(a => `<a href="https://github.com/${a.reviewer.login}" target="_blank" class="approver-link"><img src="https://github.com/${a.reviewer.login}.png" alt="${a.reviewer.login}" class="avatar rounded-full"/><span class="font-medium text-sm">${a.reviewer.login}</span></a>`).join('') :
                '<span class="text-sm text-secondary">No approvals yet.</span>';

            let timelineItems = [{
                status: 'created',
                date: pr.opened_at,
                text: 'Created'
            }];
            if (firstApproval) timelineItems.push({
                status: 'approved',
                date: firstApproval.submitted_at,
                text: '1st Approval'
            });
            if (secondApproval) timelineItems.push({
                status: 'approved',
                date: secondApproval.submitted_at,
                text: '2nd Approval'
            });
            if (pr.merged_at) timelineItems.push({
                status: 'merged',
                date: pr.merged_at,
                text: 'Merged'
            });
            timelineItems.sort((a, b) => new Date(a.date) - new Date(b.date));

            let subMetaHtml = '';
            if (pr.merged_at) {
                subMetaHtml = `<div class="pr-sub-meta status-merged-text">Merged on ${formatDate(pr.merged_at)}</div>`;
            } else if (pr.status === 'closed') {
                subMetaHtml = `<div class="pr-sub-meta status-closed-text">Closed</div>`;
            }

            return `
                <div class="pr-row" data-status="${filterStatus}" data-author="${pr.creator.login}" data-text="${pr.title.toLowerCase()} #${pr.pr_number}">
                    <div class="pr-row-main">
                        <div class="pr-info-cell">
                            <div class="pr-title"><a href="${pr.url}" target="_blank">#${pr.pr_number} ${pr.title}</a></div>
                            <div class="pr-meta">Opened on ${formatDate(pr.opened_at)}</div>
                            ${subMetaHtml}
                        </div>
                        <div class="pr-author-cell"><img src="https://github.com/${pr.creator.login}.png" alt="${pr.creator.login}" class="avatar"/><span class="text-secondary">${pr.creator.login}</span></div>
                        <div class="pr-status-badge status-${pr.status}">${pr.status}</div>
                        <div class="stats-item"><span class="added">+${pr.diff_stats.additions || 0}</span><span class="removed">-${pr.diff_stats.deletions || 0}</span><span class="changed"><svg fill="none" viewBox="0 0 24 24" stroke-width="1.7" stroke="currentColor" class="size-4"><path stroke-linecap="round" stroke-linejoin="round" d="M19.5 14.25v-2.625a3.375 3.375 0 0 0-3.375-3.375h-1.5A1.125 1.125 0 0 1 13.5 7.125v-1.5a3.375 3.375 0 0 0-3.375-3.375H8.25m0 12.75h7.5m-7.5 3H12M10.5 2.25H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 0 0-9-9Z" /></svg>${pr.diff_stats.changed_files || 0}</span></div>
                        <div class="pr-details-toggle"><svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path></svg></div>
                    </div>
                    <div class="pr-row-details"><div class="details-grid">
                        <div class="detail-section"><h4>Timeline</h4><div class="relative pt-2">${timelineItems.map(item => `<div class="timeline-item" data-status="${item.status}"><div class="timeline-line"></div><div class="timeline-marker"></div><strong>${item.text}</strong><div class="text-sm text-secondary">${formatDate(item.date)}</div></div>`).join('')}</div></div>
                        <div class="detail-section"><h4>Approvers</h4><div class="approvals-list">${approversHtml}</div></div>
                        <div class="detail-section"><h4>Key Metrics</h4><div class="space-y-3 text-sm">
                            <div class="metric-item"><svg viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm.75-13a.75.75 0 00-1.5 0v5c0 .414.336.75.75.75h4a.75.75 0 000-1.5h-3.25V5z" clip-rule="evenodd" /></svg><div><strong>Time to 1st Approval:</strong><br>${formatDuration(pr.time_to_first_approval_minutes)}</div></div>
                            <div class="metric-item"><svg viewBox="0 0 20 20" fill="currentColor"><path d="M10 2a.75.75 0 01.75.75v1.5a.75.75 0 01-1.5 0v-1.5A.75.75 0 0110 2zM10 15a.75.75 0 01.75.75v1.5a.75.75 0 01-1.5 0v-1.5A.75.75 0 0110 15zM10 7a3 3 0 100 6 3 3 0 000-6z" /></svg><div><strong>1st → 2nd Approval:</strong><br>${formatDuration(timeBetweenApprovals)}</div></div>
                            <div class="metric-item"><svg viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.857-9.809a.75.75 0 00-1.214-.882l-3.483 4.79-1.88-1.88a.75.75 0 10-1.06 1.061l2.5 2.5a.75.75 0 001.137-.089l4-5.5z" clip-rule="evenodd" /></svg><div><strong>Time to Merge:</strong><br>${formatDuration(pr.merged_at ? (new Date(pr.merged_at) - new Date(pr.opened_at)) / 60000 : null)}</div></div>
                        </div></div>
                    </div></div></div>`;
        }).join('');
    };

    const updatePrView = () => {
        const groupedPrs = allPrData.reduce((acc, pr) => {
            const weekStartDate = getWeekStartDate(pr.opened_at);
            const weekKey = weekStartDate.toISOString();
            if (!acc[weekKey]) {
                acc[weekKey] = [];
            }
            acc[weekKey].push(pr);
            return acc;
        }, {});

        const sortedWeeks = Object.keys(groupedPrs).sort((a, b) => new Date(b) - new Date(a));

        const tableHeaderHtml = `
            <div class="pr-table-header">
                <span class="sortable-header" data-sort="title">Pull Request</span>
                <span class="sortable-header" data-sort="author">Author</span>
                <span class="sortable-header" data-sort="status">Status</span>
                <span class="sortable-header" data-sort="diff">Diff</span>
                <span></span>
            </div>`;

        let finalHtml = '';
        sortedWeeks.forEach((weekKey, index) => {
            const weekStartDate = new Date(weekKey);
            const weekPrs = groupedPrs[weekKey];
            const isCollapsed = index > 0;

            const sortedData = [...weekPrs].sort((a, b) => {
                const key = sortState.key;
                const dir = sortState.direction === 'asc' ? 1 : -1;
                let valA, valB;
                switch (key) {
                    case 'title':
                        valA = a.pr_number;
                        valB = b.pr_number;
                        return (valA - valB) * dir;
                    case 'author':
                        valA = a.creator.login.toLowerCase();
                        valB = b.creator.login.toLowerCase();
                        return valA.localeCompare(valB) * dir;
                    case 'status':
                        valA = a.status.toLowerCase();
                        valB = b.status.toLowerCase();
                        return valA.localeCompare(valB) * dir;
                    case 'diff':
                        valA = (a.diff_stats.additions || 0) + (a.diff_stats.deletions || 0);
                        valB = (b.diff_stats.additions || 0) + (b.diff_stats.deletions || 0);
                        return (valA - valB) * dir;
                    case 'opened_at':
                    default:
                        valA = new Date(a.opened_at);
                        valB = new Date(b.opened_at);
                        return (valA - valB) * dir * -1;
                }
            });

            finalHtml += `
                <div class="pr-week-group ${isCollapsed ? 'collapsed' : ''}">
                    <h3 class="pr-week-header" title="${formatWeekHeader(weekStartDate)}">${getRelativeWeekName(index)}</h3>
                    <div class="pr-week-content">
                        ${tableHeaderHtml}
                        ${generatePrListHtml(sortedData)}
                    </div>
                </div>
            `;
        });

        prMetricsContainer.innerHTML = `<div class="pr-table-wrapper">${finalHtml}</div>`;

        prMetricsContainer.querySelectorAll('.pr-row-main').forEach(row => row.addEventListener('click', (e) => {
            if (e.target.closest('a')) return;
            row.parentElement.classList.toggle('details-expanded')
        }));

        prMetricsContainer.querySelectorAll('.pr-table-header').forEach(header => {
            header.addEventListener('click', handleSortClick);
        });

        prMetricsContainer.querySelectorAll('.pr-week-header').forEach(header => {
            header.addEventListener('click', () => {
                header.parentElement.classList.toggle('collapsed');
            });
        });

        updateSortHeaders();
        applyFilters();
    };

    const handleSortClick = (e) => {
        const header = e.target.closest('.sortable-header');
        if (!header) return;

        const newKey = header.dataset.sort;
        if (sortState.key === newKey) {
            sortState.direction = sortState.direction === 'asc' ? 'desc' : 'asc';
        } else {
            sortState.key = newKey;
            sortState.direction = 'asc';
        }
        updatePrView();
    };

    const updateSortHeaders = () => {
        prMetricsContainer.querySelectorAll('.sortable-header').forEach(header => {
            header.classList.remove('sorted-asc', 'sorted-desc');
            if (header.dataset.sort === sortState.key) {
                header.classList.add(sortState.direction === 'asc' ? 'sorted-asc' : 'sorted-desc');
            }
        });
    };

    const applyFilters = () => {
        const activeStatus = document.querySelector('.filter-btn.active').dataset.status;
        const activeAuthor = authorFilter.value;
        const searchText = searchInput.value.toLowerCase();

        const allRows = Array.from(prMetricsContainer.querySelectorAll('.pr-row'));

        const baseFilteredRows = allRows.filter(item => {
            const isAuthorMatch = activeAuthor === 'all' || item.dataset.author === activeAuthor;
            const isTextMatch = item.dataset.text.includes(searchText);
            return isAuthorMatch && isTextMatch;
        });

        const counts = {
            all: baseFilteredRows.length,
            merged: 0,
            pending: 0,
            closed: 0
        };
        baseFilteredRows.forEach(row => {
            if (counts.hasOwnProperty(row.dataset.status)) {
                counts[row.dataset.status]++;
            }
        });

        filterButtons.forEach(btn => {
            const status = btn.dataset.status;
            const btnText = btn.textContent.split(' ')[0];
            btn.innerHTML = `${btnText} <span class="filter-count">(${counts[status]})</span>`;
        });

        allRows.forEach(item => item.style.display = 'none');

        const finalVisibleRows = (activeStatus === 'all') ?
            baseFilteredRows :
            baseFilteredRows.filter(item => item.dataset.status === activeStatus);

        finalVisibleRows.forEach(item => item.style.display = 'flex');

        prMetricsContainer.querySelectorAll('.pr-week-group').forEach(group => {
            const visibleRowsInGroup = group.querySelector('.pr-row[style*="flex"]');
            group.style.display = visibleRowsInGroup ? 'block' : 'none';
        });
    };

    const setupEventListeners = () => {
        document.querySelector('.filter-buttons').addEventListener('click', (e) => {
            const btn = e.target.closest('.filter-btn');
            if (btn) {
                document.querySelector('.filter-btn.active').classList.remove('active');
                btn.classList.add('active');
                applyFilters();
            }
        });
        authorFilter.addEventListener('change', applyFilters);
        searchInput.addEventListener('keyup', applyFilters);
    };

    const renderPrMetrics = (data) => {
        allPrData = data;
        const authors = [...new Set(data.map(pr => pr.creator.login))].sort((a, b) => a.toLowerCase().localeCompare(b.toLowerCase()));
        authorFilter.innerHTML = `<option value="all">All Authors</option>`;
        authors.forEach(author => {
            const option = document.createElement('option');
            option.value = author;
            option.textContent = author;
            authorFilter.appendChild(option);
        });
        setupEventListeners();
        updatePrView();
    };

    const recalculateAndRenderAnalytics = (data) => {
        const todayString = new Date().toLocaleDateString('en-CA');
        const mergedPRs = data.filter(pr => pr.status === 'merged');
        document.getElementById('kpi-total-prs').textContent = data.length;
        document.getElementById('kpi-total-closed').textContent = data.filter(pr => pr.status === 'closed').length;
        document.getElementById('kpi-total-merged').textContent = mergedPRs.length;

        const totalAdditions = data.reduce((sum, pr) => sum + (pr.diff_stats.additions || 0), 0);
        const totalDeletions = data.reduce((sum, pr) => sum + (pr.diff_stats.deletions || 0), 0);
        const avgAdditions = data.length > 0 ? (totalAdditions / data.length).toFixed(0) : 0;
        const avgDeletions = data.length > 0 ? (totalDeletions / data.length).toFixed(0) : 0;
        document.getElementById('kpi-avg-pr-size').innerHTML = data.length > 0 ? `<span class="added">+${avgAdditions}</span> <span class="removed">-${avgDeletions}</span>` : '-';

        const approvalTimes = data.map(pr => pr.time_to_first_approval_minutes).filter(t => t !== null && t >= 0);
        document.getElementById('kpi-avg-approval-time').textContent = formatDuration(approvalTimes.length ? approvalTimes.reduce((a, b) => a + b, 0) / approvalTimes.length : null);

        const lifespans = mergedPRs.map(pr => (new Date(pr.merged_at) - new Date(pr.opened_at)) / 60000).filter(t => t > 0);
        document.getElementById('kpi-avg-lifespan').textContent = formatDuration(lifespans.length ? lifespans.reduce((a, b) => a + b, 0) / lifespans.length : null);

        const recentActivity = data.reduce((acc, pr) => {
            acc[pr.creator.login] = (acc[pr.creator.login] || 0) + 1;
            if (pr.merged_by) acc[pr.merged_by.login] = (acc[pr.merged_by.login] || 0) + 1;
            return acc;
        }, {});
        const hotStreakUser = Object.entries(recentActivity).sort((a, b) => b[1] - a[1])[0];
        document.getElementById('hot-streak-card').innerHTML = hotStreakUser ? `<div class="kpi-icon bg-orange-500"><svg viewBox="0 0 24 24" fill="currentColor"><path d="M11.163 2.006a2.008 2.008 0 0 1 1.674 0 23.33 23.33 0 0 0 3.332.943c.42.115.823.383 1.12.756.298.373.473.85.49 1.349.02.535-.118 1.063-.393 1.503-.276.44-.678.78-1.144 1.004a25.34 25.34 0 0 1-4.242 1.445c-.486.136-1.003.136-1.489 0a25.34 25.34 0 0 1-4.242-1.445c-.466-.223-.868-.564-1.144-1.004-.275-.44-.413-1.028-.393-1.563.017-.499.192-.976.49-1.349.297-.373.7-.64 1.12-.756a23.33 23.33 0 0 0 3.332-.943ZM12 11a3 3 0 0 1 3 3c0 1.25-.75 3.5-3 5.25-2.25-1.75-3-4-3-5.25a3 3 0 0 1 3-3Z"></path></svg></div><div class="overflow-hidden"><div class="kpi-value"><img src="https://github.com/${hotStreakUser[0]}.png" class="avatar"/> <span class="truncate">${hotStreakUser[0]}</span></div><div class="kpi-label">Top Contributor (${hotStreakUser[1]} actions)</div></div>` : `<div class="p-4 text-center">No activity.</div>`;

        const recentReviews = data.reduce((acc, pr) => {
            if (pr.time_to_first_approval_minutes !== null) {
                const r = (pr.approvals || []).sort((a, b) => new Date(a.submitted_at) - new Date(b.submitted_at))[0]?.reviewer.login;
                if (r)(acc[r] = acc[r] || []).push(pr.time_to_first_approval_minutes);
            }
            return acc;
        }, {});
        const fastestData = Object.entries(recentReviews).map(([u, t]) => ({
            user: u,
            avg: t.reduce((a, b) => a + b, 0) / t.length
        })).sort((a, b) => a.avg - b.avg)[0];
        document.getElementById('fastest-reviewer-card').innerHTML = fastestData ? `<div class="kpi-icon bg-teal-500"><svg viewBox="0 0 24 24" fill="currentColor"><path fill-rule="evenodd" d="M12.963 2.286a.75.75 0 0 0-1.071 1.052A32.11 32.11 0 0 1 12 11.897a.75.75 0 0 1-1.5 0 33.61 33.61 0 0 0-1.433-8.56A.75.75 0 0 0 7.963 2.286 33.337 33.337 0 0 0 3 11.25a.75.75 0 0 0 1.5 0c0-1.88.243-3.727.712-5.522A31.838 31.838 0 0 1 12 21.75c2.784 0 5.488-.344 8.088-.98a.75.75 0 0 0-.21-1.474c-1.92.44-3.926.654-5.978.654-3.776 0-7.398-1.026-10.68-2.852A31.838 31.838 0 0 1 2.288 5.728a.75.75 0 0 0-1.052-1.07C.4 5.478 0 6.474 0 7.5c0 1.573.493 3.091 1.369 4.417.876 1.326 2.087 2.41 3.533 3.193a.75.75 0 0 0 .937-.587c.106-.49.227-.978.362-1.463a.75.75 0 0 0-.584-.863 15.65 15.65 0 0 1-1.123-.424c.2-.55.43-1.096.683-1.636.76 1.11 1.835 1.99 3.167 2.61a.75.75 0 0 0 .93-.593c.08-.39.15-.783.21-1.179a.75.75 0 0 0-.55-.838c-.375-.123-.74-.25-1.091-.383.612-.953 1.08-2.01 1.38-3.155a.75.75 0 0 0-.68-.82 14.89 14.89 0 0 1-1.28-.21C9.69 6.27 11.27 3.5 12.963 2.286Z" clip-rule="evenodd"></path></svg></div><div class="overflow-hidden"><div class="kpi-value"><img src="https://github.com/${fastestData.user}.png" class="avatar"/> <span class="truncate">${fastestData.user}</span></div><div class="kpi-label">Fastest Reviewer (${formatDuration(fastestData.avg)})</div></div>` : `<div class="p-4 text-center">No recent reviews.</div>`;

        // --- HEALTH LOGIC ---
        // Chart data is always based on the filtered data set (`data` parameter)
        const chartMergeTimes = mergedPRs
            .filter(pr => pr.merged_at && pr.required_approvals_met_at)
            .reduce((acc, pr) => {
                const day = new Date(pr.merged_at).toLocaleDateString('en-CA');
                const time = (new Date(pr.merged_at) - new Date(pr.required_approvals_met_at)) / 60000;
                if (time >= 0) {
                    if (!acc[day]) acc[day] = [];
                    acc[day].push(time);
                }
                return acc;
            }, {});

        const chartAvgMergeTimes = Object.entries(chartMergeTimes).map(([day, times]) => ({
            day,
            avg: times.reduce((a, b) => a + b, 0) / times.length
        })).sort((a, b) => new Date(a.day) - new Date(b.day));

        let healthMessage = `<div class="health-indicator info">${ICONS.safe}<span>Merge process times for this period are stable.</span></div>`;
        const isAllTimeView = analyticsWeekFilter.value === 'all';

        // Trend analysis message is only calculated for "All Time" view
        if (isAllTimeView) {
            // Use the full dataset (`allPrData`) specifically for this trend calculation
            const allMergeTimes = allPrData
                .filter(pr => pr.status === 'merged' && pr.merged_at && pr.required_approvals_met_at)
                .reduce((acc, pr) => { /* ... same reduce logic ... */
                    const day = new Date(pr.merged_at).toLocaleDateString('en-CA');
                    const time = (new Date(pr.merged_at) - new Date(pr.required_approvals_met_at)) / 60000;
                    if (time >= 0) {
                        if (!acc[day]) acc[day] = [];
                        acc[day].push(time);
                    }
                    return acc;
                }, {});
            const allAvgMergeTimes = Object.entries(allMergeTimes).map(([day, times]) => ({
                day,
                avg: times.reduce((a, b) => a + b, 0) / times.length
            })).sort((a, b) => new Date(a.day) - new Date(b.day));

            healthMessage = `<div class="health-indicator info">${ICONS.safe}<span>Merge process times are stable. Keep up the good work!</span></div>`;
            if (allAvgMergeTimes.length >= 7) {
                const lastWeekAvg = allAvgMergeTimes.slice(-7).reduce((a, b) => a + b.avg, 0) / 7;
                const prevWeekAvg = allAvgMergeTimes.slice(-14, -7).reduce((a, b) => a + b.avg, 0) / 7;
                if (prevWeekAvg > 0) {
                    const change = (lastWeekAvg - prevWeekAvg) / prevWeekAvg * 100;
                    if (change > 15) healthMessage = `<div class="health-indicator warning">${ICONS.warning}<span>Merge process is <strong>${change.toFixed(0)}% slower</strong> this week. Let's pick up the pace!</span></div>`;
                    else if (change < -15) healthMessage = `<div class="health-indicator success">${ICONS.safe}<span>Awesome! Merge process is <strong>${Math.abs(change).toFixed(0)}% faster</strong> this week!</span></div>`;
                }
            }
        }

        if (chartAvgMergeTimes.length > 0) {
            document.getElementById('merge-process-health-container').innerHTML = `<h3 class="panel-header panel-title">Merge Process Health</h3><div class="panel-body">${healthMessage}<div class="chart-container-sm"><canvas id="mergeProcessHealthChart"></canvas></div></div>`;
        } else {
            document.getElementById('merge-process-health-container').innerHTML = `<h3 class="panel-header panel-title">Merge Process Health</h3><div class="panel-body"><div class="health-indicator info">No merge data for this period.</div></div>`;
        }
        // --- END OF CORRECTED LOGIC ---

        const dailyActivity = data.reduce((acc, pr) => {
            const day = new Date(pr.opened_at).toLocaleDateString('en-CA');
            if (!acc[day]) acc[day] = {
                created: [],
                merged: 0,
                approvals: 0
            };
            acc[day].created.push(pr);
            if (pr.merged_at) {
                const mDay = new Date(pr.merged_at).toLocaleDateString('en-CA');
                if (!acc[mDay]) acc[mDay] = {
                    created: [],
                    merged: 0,
                    approvals: 0
                };
                acc[mDay].merged++
            }(pr.approvals || []).forEach(a => {
                const aDay = new Date(a.submitted_at).toLocaleDateString('en-CA');
                if (!acc[aDay]) acc[aDay] = {
                    created: [],
                    merged: 0,
                    approvals: 0
                };
                acc[aDay].approvals++
            });
            return acc
        }, {});
        const allDays = Object.keys(dailyActivity).sort((a, b) => new Date(b) - new Date(a));
        document.getElementById('daily-breakdown-container').innerHTML = allDays.length > 0 ? `<div class="daily-breakdown-grid">${allDays.slice(0, 14).map(day => { const { created, merged, approvals } = dailyActivity[day]; const isToday = day === todayString; return `<div class="day-card ${isToday ? 'today' : ''}" title="${new Date(day).toLocaleDateString()}"><div class="day-label">${new Date(day).toLocaleString('en-US', { weekday: 'short' })}</div><div class="day-number">${new Date(day).getDate()}</div><div class="day-stats">${created.length > 0 ? `<div class="day-stat-item created"><strong>${created.length}</strong> <span>Opened</span></div>` : ''}${merged > 0 ? `<div class="day-stat-item merged"><strong>${merged}</strong> <span>Merged</span></div>` : ''}${approvals > 0 ? `<div class="day-stat-item approvals"><strong>${approvals}</strong> <span>Reviews</span></div>` : ''}</div></div>` }).join('')}</div>` : `<div class="p-4 text-center">No daily activity data for this period.</div>`;

        const prTypes = data.reduce((acc, pr) => {
            const title = pr.title.toLowerCase();
            let type = 'other';
            if (title.startsWith('fix')) type = 'fix';
            else if (title.startsWith('feat')) type = 'feature';
            else if (title.startsWith('chore')) type = 'chore';
            else if (title.startsWith('refactor')) type = 'refactor';
            else if (title.startsWith('docs')) type = 'docs';
            if (!acc[type]) acc[type] = {
                count: 0,
                additions: 0,
                deletions: 0,
                approvalTimes: []
            };
            acc[type].count++;
            acc[type].additions += pr.diff_stats.additions || 0;
            acc[type].deletions += pr.diff_stats.deletions || 0;
            if (pr.time_to_first_approval_minutes !== null) acc[type].approvalTimes.push(pr.time_to_first_approval_minutes);
            return acc
        }, {});
        const prTypeColors = {
            fix: '#ef4444',
            feature: '#3b82f6',
            chore: '#6b7280',
            refactor: '#f97316',
            docs: '#14b8a6',
            other: '#a855f7'
        };
        const prTypeEntries = Object.entries(prTypes);
        document.getElementById('pr-type-trends-container').innerHTML = `<h3 class="panel-header panel-title">PR Type Analysis</h3><div class="panel-body">${prTypeEntries.length > 0 ? `<div class="pr-type-grid">${prTypeEntries.sort((a,b) => b[1].count - a[1].count).map(([type, d]) => `<div class="pr-type-card"><div class="pr-type-header"><div class="pr-type-icon" style="background-color:${prTypeColors[type] || '#ccc'}"></div><div class="pr-type-title"><span class="capitalize">${type}</span><div class="pr-type-bar"><div class="pr-type-fill" style="width:${d.count / data.length * 100}%;background-color:${prTypeColors[type] || '#ccc'};"></div></div></div><span class="pr-type-count">${d.count} PRs</span></div></div>`).join('')}</div>` : `<div class="p-4 text-center">No PR type data for this period.</div>`}</div>`;

        const contributions = data.reduce((acc, pr) => {
            acc[pr.creator.login] = (acc[pr.creator.login] || {
                o: 0,
                m: 0
            });
            acc[pr.creator.login].o++;
            if (pr.merged_by) {
                acc[pr.merged_by.login] = (acc[pr.merged_by.login] || {
                    o: 0,
                    m: 0
                });
                acc[pr.merged_by.login].m++;
            }
            return acc;
        }, {});
        const chartLabels = Object.keys(contributions).sort();
        const reviewers = data.flatMap(pr => pr.approvals || []).reduce((acc, a) => {
            (acc[a.reviewer.login] = (acc[a.reviewer.login] || 0) + 1);
            return acc;
        }, {});
        const collaborationSummary = data.reduce((acc, pr) => {
            (pr.approvals || []).forEach(a => {
                const r = a.reviewer.login;
                if (pr.creator.login === r) return;
                if (!acc[r]) acc[r] = {
                    totalReviews: 0,
                    collaborators: new Set()
                };
                acc[r].totalReviews++;
                acc[r].collaborators.add(pr.creator.login)
            });
            return acc;
        }, {});
        const sortedCollaborators = Object.entries(collaborationSummary).map(([r, d]) => ({
            reviewer: r,
            ...d
        })).sort((a, b) => b.totalReviews - a.totalReviews).slice(0, 10);
        document.getElementById('collaboration-list').innerHTML = sortedCollaborators.length > 0 ? sortedCollaborators.map(item => {
            const avs = Array.from(item.collaborators).slice(0, 5).map(c => `<a href="https://github.com/${c}" target="_blank" title="${c}"><img src="https://github.com/${c}.png" class="collaborator-avatar"></a>`).join('');
            return `<li><div class="reviewer-summary"><a href="https://github.com/${item.reviewer}" target="_blank"><img src="https://github.com/${item.reviewer}.png" class="avatar"/><span>${item.reviewer}</span></a><span class="text-secondary">reviewed</span><div class="collaborator-avatar-stack">${avs}</div></div><span class="collaboration-count">${item.totalReviews} total</span></li>`;
        }).join('') : '<li>No collaboration data available for this period.</li>';

        const getOrCreateTooltip = (chart) => {
            let el = document.querySelector('.chart-tooltip');
            if (!el) {
                el = document.createElement('div');
                el.className = 'chart-tooltip';
                document.body.appendChild(el)
            }
            return el
        };
        const externalTooltipHandler = (ctx) => {
            const {
                chart,
                tooltip
            } = ctx;
            const el = getOrCreateTooltip(chart);
            if (tooltip.opacity === 0) {
                el.style.opacity = 0;
                return
            }
            if (tooltip.body) {
                const u = tooltip.dataPoints[0].label;
                el.innerHTML = `<div class="chart-tooltip-header"><img src="https://github.com/${u}.png" class="avatar"/><strong>${u}</strong></div><div class="chart-tooltip-body">${tooltip.dataPoints.map(p => `<div>${p.dataset.label}: ${p.raw}</div>`).join('')}</div>`
            }
            const pos = chart.canvas.getBoundingClientRect();
            el.style.opacity = 1;
            el.style.left = pos.left + window.scrollX + tooltip.caretX + 'px';
            el.style.top = pos.top + window.scrollY + tooltip.caretY + 'px'
        };
        if (contributionsChart) contributionsChart.destroy();
        contributionsChart = new Chart(document.getElementById('contributionsChart'), {
            type: 'bar',
            data: {
                labels: chartLabels,
                datasets: [{
                    label: 'Opened',
                    data: chartLabels.map(l => contributions[l]?.o || 0),
                    backgroundColor: 'rgba(79,70,229,0.7)'
                }, {
                    label: 'Merged',
                    data: chartLabels.map(l => contributions[l]?.m || 0),
                    backgroundColor: 'rgba(16,185,129,0.7)'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    x: {
                        stacked: true
                    },
                    y: {
                        stacked: true,
                        beginAtZero: true
                    }
                },
                plugins: {
                    legend: {
                        position: 'bottom'
                    },
                    tooltip: {
                        enabled: false,
                        external: externalTooltipHandler
                    }
                }
            }
        });
        const sortedReviewers = Object.entries(reviewers).sort((a, b) => b[1] - a[1]);
        if (reviewersChart) reviewersChart.destroy();
        reviewersChart = new Chart(document.getElementById('reviewersChart'), {
            type: 'bar',
            data: {
                labels: sortedReviewers.map(r => r[0]),
                datasets: [{
                    label: 'Approvals',
                    data: sortedReviewers.map(r => r[1]),
                    backgroundColor: 'rgba(2,132,199,0.7)'
                }]
            },
            options: {
                indexAxis: 'y',
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false
                    },
                    tooltip: {
                        enabled: false,
                        external: externalTooltipHandler
                    }
                }
            }
        });

        if (mergeProcessHealthChart) mergeProcessHealthChart.destroy();
        if (chartAvgMergeTimes.length > 0) {
            mergeProcessHealthChart = new Chart(document.getElementById('mergeProcessHealthChart'), {
                type: 'line',
                data: {
                    labels: chartAvgMergeTimes.map(d => d.day),
                    datasets: [{
                        label: 'Time (Approval to Merge)',
                        data: chartAvgMergeTimes.map(d => d.avg),
                        borderColor: '#f97316',
                        tension: 0.3,
                        fill: true,
                        backgroundColor: 'rgba(249,115,22,0.1)'
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            display: false
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: 'Minutes'
                            }
                        }
                    }
                }
            });
        }
        applyTheme(localStorage.getItem('theme') || 'light');
    };

    const renderAnalytics = (data) => {
        const weekKeys = [...new Set(data.map(pr => getWeekStartDate(pr.opened_at).toISOString()))]
            .sort((a, b) => new Date(b) - new Date(a));

        analyticsWeekFilter.innerHTML = `<option value="all">All Time</option>`;
        weekKeys.forEach((key, index) => {
            const option = document.createElement('option');
            option.value = key;
            option.textContent = getRelativeWeekName(index);
            analyticsWeekFilter.appendChild(option);
        });

        analyticsWeekFilter.addEventListener('change', () => {
            const selectedWeek = analyticsWeekFilter.value;
            if (selectedWeek === 'all') {
                recalculateAndRenderAnalytics(allPrData);
            } else {
                const filteredData = allPrData.filter(pr => getWeekStartDate(pr.opened_at).toISOString() === selectedWeek);
                recalculateAndRenderAnalytics(filteredData);
            }
        });

        if (weekKeys.length > 0) {
            analyticsWeekFilter.value = weekKeys[0];
            analyticsWeekFilter.dispatchEvent(new Event('change'));
        } else {
            recalculateAndRenderAnalytics(data);
        }
    };

    const renderRatings = (data, lang = 'en') => {
        const t = translations[lang];

        const formatSkillName = (name) => {
            return t[name.toLowerCase()] || name.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase());
        };

        const getScoreColor = (score) => {
            if (score >= 4.0) return '#22c55e'; // Strong
            if (score >= 2.6) return '#f59e0b'; // Developing
            return '#ef4444'; // Needs Improvement
        };

        const getAverageFromScoreObject = (scoreObj) => {
            const scoresOnly = { ...scoreObj };
            delete scoresOnly.submitted_at;
            const scoreValues = Object.values(scoresOnly);
            if (scoreValues.length === 0) return 0;
            return scoreValues.reduce((a, b) => a + b, 0) / scoreValues.length;
        }

        const ICONS_RATINGS = {
            summary: `<svg class="panel-header-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>`,
            categories: `<svg class="panel-header-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h7"></path></svg>`,
            comments: `<svg class="panel-header-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"></path></svg>`,
            progress: `<svg class="panel-header-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6"></path></svg>`,
            notes: `<svg class="panel-header-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path></svg>`,
            tips: `<svg class="panel-header-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z"></path></svg>`
        };

        if (!data || data.length === 0) {
            ratingsContainer.innerHTML = `<div class="p-4 text-center">No ratings data available.</div>`;
            return;
        }

        // Sort mentees by average rating before rendering
        data.sort((a, b) => {
            const avgA = a.scores.length > 0 ? getAverageFromScoreObject(a.scores[a.scores.length - 1]) : 0;
            const avgB = b.scores.length > 0 ? getAverageFromScoreObject(b.scores[b.scores.length - 1]) : 0;
            return avgB - avgA;
        });

        const introNoteHtml = `
            <div class="feedback-summary" style="margin-bottom: 2rem;">
                <p><strong>${t.introTitle}</strong> ${t.introBody}</p>
            </div>
        `;

        const cardsHtml = data.map(mentee => {
            const sortedScores = (mentee.scores || []).sort((a, b) => new Date(a.submitted_at) - new Date(b.submitted_at));
            if (sortedScores.length === 0) return '';

            const latestScoreEntry = sortedScores[sortedScores.length - 1];
            const previousScoreEntry = sortedScores.length > 1 ? sortedScores[sortedScores.length - 2] : null;
            const latestScores = { ...latestScoreEntry };
            delete latestScores.submitted_at;
            const averageScore = getAverageFromScoreObject(latestScoreEntry);

            let trendHtml = `<p class="trend">${t.firstEval}</p>`;
            if (previousScoreEntry) {
                const prevAverage = getAverageFromScoreObject(previousScoreEntry);
                const trend = averageScore - prevAverage;
                const trendClass = trend >= 0 ? 'trend-up' : 'trend-down';
                const trendSign = trend >= 0 ? '▲ +' : '▼ ';
                trendHtml = `<p class="trend ${trendClass}">${trendSign}${Math.abs(trend.toFixed(1))} ${t.fromLastEval}</p>`;
            }

            const lowestSkill = Object.entries(latestScores).sort((a, b) => a[1] - b[1])[0];
            const peerCommentsHtml = mentee.peer_comments && mentee.peer_comments.length > 0 ?
                mentee.peer_comments.map(comment => `<li>${comment[lang]}</li>`).join('') : `<li>${t.noPeerComments}</li>`;
            const personalNotesHtml = mentee.personal_notes && mentee.personal_notes.length > 0 ?
                mentee.personal_notes.map(note => `<li>${note[lang]}</li>`).join('') : `<li>${t.noPersonalNotes}</li>`;

            const categoryBarsHtml = Object.entries(latestScores).sort((a,b)=> b[1] - a[1]).map(([skill, score]) => `
                 <div class="category-item">
                    <div class="category-item-title">
                        <span>${formatSkillName(skill)}</span>
                        <span style="color: ${getScoreColor(score)}">${score.toFixed(1)}</span>
                    </div>
                    <div class="category-item-bar">
                        <div class="category-item-fill" style="width: ${score / 5 * 100}%; background-color: ${getScoreColor(score)};"></div>
                    </div>
                </div>
            `).join('');

            const tipKey = `tip_${lowestSkill[0]}`;
            const suggestionText = t[tipKey] || "Keep up the great work across all areas!";

            return `
                <div class="rating-card" id="rating-card-${mentee.id}">
                    <div class="rating-card-header" dir="ltr">
                        <img src="https://github.com/${mentee.login}.png" alt="${mentee.login}" class="avatar"/>
                        <div>
                             <h3 class="mentee-name">${mentee.name}</h3>
                             <p class="mentee-login">@${mentee.login}</p>
                        </div>
                    </div>
                    <div class="rating-card-details">
                        <div class="details-wrapper">
                            <div class="details-grid-ratings">
                               <div class="rating-panel">
                                    <h4>${ICONS_RATINGS.summary} ${t.overallPerformance}</h4>
                                    <p class="average-score">${averageScore.toFixed(1)} <span class="text-lg text-secondary">/ 5</span></p>
                                    ${trendHtml}
                                    <p class="tagline">${t.tagline}</p>
                                    <div class="chart-container-radar">
                                        <canvas id="radar-chart-${mentee.id}"></canvas>
                                    </div>
                               </div>
                               <div class="rating-panel">
                                    <h4>${ICONS_RATINGS.categories} ${t.categoryBreakdown}</h4>
                                    <div class="category-list">${categoryBarsHtml}</div>
                               </div>
                               <div class="rating-panel comment-notes-panel">
                                    <h4>${ICONS_RATINGS.comments} ${t.peerComments}</h4>
                                    <ul class="comment-notes-list">${peerCommentsHtml}</ul>
                               </div>
                               <div class="rating-panel comment-notes-panel">
                                    <h4>${ICONS_RATINGS.notes} ${t.personalNotes}</h4>
                                    <ul class="comment-notes-list">${personalNotesHtml}</ul>
                               </div>
                                <div class="rating-panel">
                                    <h4>${ICONS_RATINGS.progress} ${t.performanceOverTime}</h4>
                                    <div class="chart-container-sm">
                                        <canvas id="progress-chart-${mentee.id}"></canvas>
                                    </div>
                               </div>
                               <div class="rating-panel">
                                    <h4>${ICONS_RATINGS.tips} ${t.suggestionBox}</h4>
                                    <div class="tips-box">
                                        <strong>${t.focusArea}: ${formatSkillName(lowestSkill[0])}</strong>
                                        <p>${suggestionText}</p>
                                    </div>
                               </div>
                            </div>
                        </div>
                    </div>
                </div>
            `;
        }).join('');

        ratingsContainer.innerHTML = introNoteHtml + `<div class="ratings-grid">${cardsHtml}</div>`;

        progressCharts = [];
        radarCharts = [];
        data.forEach(mentee => {
            const card = document.getElementById(`rating-card-${mentee.id}`);
            if(!card) return;

            const header = card.querySelector('.rating-card-header');
            header.addEventListener('click', () => {
                card.classList.toggle('expanded');
            });

            const sortedScores = (mentee.scores || []).sort((a, b) => new Date(a.submitted_at) - new Date(b.submitted_at));
            if (sortedScores.length === 0) return;

            const latestScoreEntry = sortedScores[sortedScores.length - 1];
            const latestScores = { ...latestScoreEntry };
            delete latestScores.submitted_at;

            const radarChart = new Chart(document.getElementById(`radar-chart-${mentee.id}`), {
                type: 'radar',
                data: {
                    labels: Object.keys(latestScores).map(formatSkillName),
                    datasets: [{
                        data: Object.values(latestScores),
                        fill: true,
                        backgroundColor: 'rgba(79, 70, 229, 0.2)',
                        borderColor: 'rgb(79, 70, 229)',
                        pointBackgroundColor: 'rgb(79, 70, 229)',
                    }]
                },
                options: {
                    responsive: true, maintainAspectRatio: false,
                    scales: { r: { suggestedMin: 0, suggestedMax: 5, ticks: { display: false } } },
                    plugins: { legend: { display: false } }
                }
            });
            radarCharts.push(radarChart);

            const progressData = sortedScores.map(entry => getAverageFromScoreObject(entry));
            const progressLabels = sortedScores.map(entry => new Date(entry.submitted_at).toLocaleDateString(lang === 'ar' ? 'ar-EG' : 'en-US', {
                month: 'short',
                day: 'numeric'
            }));

            const progressChart = new Chart(document.getElementById(`progress-chart-${mentee.id}`), {
                type: 'line',
                data: {
                    labels: progressLabels,
                    datasets: [{
                        label: t.avgScore,
                        data: progressData,
                        borderColor: 'rgb(79, 70, 229)',
                        tension: 0.1,
                        fill: true,
                        backgroundColor: 'rgba(79, 70, 229, 0.1)'
                    }]
                },
                options: {
                    responsive: true, maintainAspectRatio: false,
                    scales: { y: { beginAtZero: true, max: 5 } },
                    plugins: { legend: { display: false } }
                }
            });
            progressCharts.push(progressChart);
        });

        applyTheme(localStorage.getItem('theme') || 'light');
    };

    const renderFeedback = (data) => {
        feedbackContainer.innerHTML = data.improvement_topics.map(t => `<div class="panel feedback-topic-card"><h2 class="panel-header panel-title">${t.topic_title}</h2><div class="content-pair"><div class="problem-section feedback-section"><h3>${ICONS.warning} ${t.problem_section.title}</h3><ul>${t.problem_section.points.map(p => `<li>${p}</li>`).join('')}</ul></div>${t.solution_section ? `<div class="solution-section feedback-section"><h3>${ICONS.lightbulb} ${t.solution_section.title}</h3><ul>${t.solution_section.points.map(p => `<li>${p}</li>`).join('')}</ul></div>` : ''}</div></div>`).join('') + `<div class="feedback-summary">${data.summary}</div>`
    };

    const main = async () => {
        const initialLang = localStorage.getItem('language') || 'en';
        setLanguage(initialLang);

        const theme = localStorage.getItem('theme') || (window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light');
        applyTheme(theme);
        var baseUrl = "https://raw.githubusercontent.com/LondonSquad/Novix/refs/heads/metrics/";
        try {
            const [pr, fb, ratings] = await Promise.all([
                fetch(baseUrl + "pr_metrics.json").then(r => r.ok ? r.json() : Promise.reject(r)),
                fetch(baseUrl + "feedbacks.json").then(r => r.ok ? r.json() : Promise.reject(r)),
                fetch(baseUrl + "mentees_ratings.json").then(r => r.ok ? r.json() : Promise.reject(r))
            ]);
            allRatingsData = ratings; // Store globally
            statusArea.style.display = 'none';
            contentArea.classList.remove("hidden");
            renderPrMetrics(pr);
            renderAnalytics(pr);
            renderRatings(ratings, initialLang); // Initial render with correct language
            renderFeedback(fb)
        } catch (e) {
            console.error("Error fetching dashboard data:", e);
            statusArea.innerHTML = `<div class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4" role="alert"><p class="font-bold">Loading Failed</p><p>Could not fetch required data. Ensure files are accessible.</p></div>`
        }
    };
    main();

});
