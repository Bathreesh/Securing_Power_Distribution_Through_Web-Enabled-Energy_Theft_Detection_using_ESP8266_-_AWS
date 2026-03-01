// app.js - Dashboard JavaScript

// ===== SETTINGS MANAGER =====
const settingsManager = {
    defaults: {
        theme: 'dark',
        accentColor: 'orange',
        fontSize: 'medium',
        autoRefresh: 5000,
        compactView: false,
        animations: true,
        alertsEnabled: true,
        alertSensitivity: 'normal',
        soundAlert: false,
        showLastUpdated: true,
        measurementUnit: 'W'
    },

    settings: {},

    load() {
        try {
            const saved = localStorage.getItem('gridSecureSettings');
            this.settings = saved ? { ...this.defaults, ...JSON.parse(saved) } : { ...this.defaults };
        } catch (e) {
            this.settings = { ...this.defaults };
        }
        return this.settings;
    },

    save() {
        try {
            localStorage.setItem('gridSecureSettings', JSON.stringify(this.settings));
        } catch (e) {
            console.error('Failed to save settings:', e);
        }
    },

    set(key, value) {
        this.settings[key] = value;
        this.save();
        this.apply();
    },

    get(key) {
        return this.settings[key];
    },

    reset() {
        this.settings = { ...this.defaults };
        this.save();
        this.apply();
        this.updateUI();
    },

    apply() {
        const root = document.documentElement;

        // Theme
        root.setAttribute('data-theme', this.settings.theme);

        // Accent Color
        root.setAttribute('data-accent', this.settings.accentColor);

        // Font Size
        root.setAttribute('data-font-size', this.settings.fontSize);

        // Compact View
        root.setAttribute('data-compact', this.settings.compactView);

        // Animations
        root.setAttribute('data-animations', this.settings.animations);

        // Update auto-refresh if needed
        if (window.updateRefreshInterval) {
            window.updateRefreshInterval(this.settings.autoRefresh);
        }
    },

    updateUI() {
        // Update theme toggle
        const themeToggle = document.getElementById('settingTheme');
        if (themeToggle) themeToggle.checked = this.settings.theme === 'light';

        // Update accent color
        const accentRadio = document.querySelector(`input[name="accentColor"][value="${this.settings.accentColor}"]`);
        if (accentRadio) accentRadio.checked = true;

        // Update font size
        const fontRadio = document.querySelector(`input[name="fontSize"][value="${this.settings.fontSize}"]`);
        if (fontRadio) fontRadio.checked = true;

        // Update auto refresh
        const refreshSelect = document.getElementById('settingAutoRefresh');
        if (refreshSelect) refreshSelect.value = this.settings.autoRefresh;

        // Update compact view
        const compactToggle = document.getElementById('settingCompactView');
        if (compactToggle) compactToggle.checked = this.settings.compactView;

        // Update animations
        const animToggle = document.getElementById('settingAnimations');
        if (animToggle) animToggle.checked = this.settings.animations;

        // Update alerts enabled
        const alertsToggle = document.getElementById('settingAlertsEnabled');
        if (alertsToggle) alertsToggle.checked = this.settings.alertsEnabled;

        // Update sensitivity
        const sensitivityRadio = document.querySelector(`input[name="alertSensitivity"][value="${this.settings.alertSensitivity}"]`);
        if (sensitivityRadio) sensitivityRadio.checked = true;

        // Update sound alert
        const soundToggle = document.getElementById('settingSoundAlert');
        if (soundToggle) soundToggle.checked = this.settings.soundAlert;

        // Update show last updated
        const lastUpdatedToggle = document.getElementById('settingShowLastUpdated');
        if (lastUpdatedToggle) lastUpdatedToggle.checked = this.settings.showLastUpdated;

        // Update measurement unit
        const unitRadio = document.querySelector(`input[name="measurementUnit"][value="${this.settings.measurementUnit}"]`);
        if (unitRadio) unitRadio.checked = true;
    }
};

// Initialize settings on load
settingsManager.load();
settingsManager.apply();

// ===== SETTINGS PANEL CONTROLS =====
function initSettingsPanel() {
    const settingsBtn = document.getElementById('settingsBtn');
    const settingsPanel = document.getElementById('settingsPanel');
    const settingsOverlay = document.getElementById('settingsOverlay');
    const settingsClose = document.getElementById('settingsClose');

    function openSettings() {
        if (settingsPanel) settingsPanel.classList.add('active');
        if (settingsOverlay) settingsOverlay.classList.add('active');
        document.body.style.overflow = 'hidden';
        settingsManager.updateUI();
    }

    function closeSettings() {
        if (settingsPanel) settingsPanel.classList.remove('active');
        if (settingsOverlay) settingsOverlay.classList.remove('active');
        document.body.style.overflow = '';
    }

    if (settingsBtn) settingsBtn.addEventListener('click', openSettings);
    if (settingsClose) settingsClose.addEventListener('click', closeSettings);
    if (settingsOverlay) settingsOverlay.addEventListener('click', closeSettings);

    // Theme toggle
    const themeToggle = document.getElementById('settingTheme');
    if (themeToggle) {
        themeToggle.addEventListener('change', (e) => {
            settingsManager.set('theme', e.target.checked ? 'light' : 'dark');
        });
    }

    // Accent color
    document.querySelectorAll('input[name="accentColor"]').forEach(radio => {
        radio.addEventListener('change', (e) => {
            settingsManager.set('accentColor', e.target.value);
        });
    });

    // Font size
    document.querySelectorAll('input[name="fontSize"]').forEach(radio => {
        radio.addEventListener('change', (e) => {
            settingsManager.set('fontSize', e.target.value);
        });
    });

    // Auto refresh
    const refreshSelect = document.getElementById('settingAutoRefresh');
    if (refreshSelect) {
        refreshSelect.addEventListener('change', (e) => {
            settingsManager.set('autoRefresh', parseInt(e.target.value));
        });
    }

    // Compact view
    const compactToggle = document.getElementById('settingCompactView');
    if (compactToggle) {
        compactToggle.addEventListener('change', (e) => {
            settingsManager.set('compactView', e.target.checked);
        });
    }

    // Animations
    const animToggle = document.getElementById('settingAnimations');
    if (animToggle) {
        animToggle.addEventListener('change', (e) => {
            settingsManager.set('animations', e.target.checked);
        });
    }

    // Alerts enabled
    const alertsToggle = document.getElementById('settingAlertsEnabled');
    if (alertsToggle) {
        alertsToggle.addEventListener('change', (e) => {
            settingsManager.set('alertsEnabled', e.target.checked);
        });
    }

    // Alert sensitivity
    document.querySelectorAll('input[name="alertSensitivity"]').forEach(radio => {
        radio.addEventListener('change', (e) => {
            settingsManager.set('alertSensitivity', e.target.value);
        });
    });

    // Sound alert
    const soundToggle = document.getElementById('settingSoundAlert');
    if (soundToggle) {
        soundToggle.addEventListener('change', (e) => {
            settingsManager.set('soundAlert', e.target.checked);
        });
    }

    // Show last updated
    const lastUpdatedToggle = document.getElementById('settingShowLastUpdated');
    if (lastUpdatedToggle) {
        lastUpdatedToggle.addEventListener('change', (e) => {
            settingsManager.set('showLastUpdated', e.target.checked);
        });
    }

    // Measurement unit
    document.querySelectorAll('input[name="measurementUnit"]').forEach(radio => {
        radio.addEventListener('change', (e) => {
            settingsManager.set('measurementUnit', e.target.value);
        });
    });

    // Reset button
    const resetBtn = document.getElementById('settingsResetBtn');
    if (resetBtn) {
        resetBtn.addEventListener('click', () => {
            if (confirm('Reset all settings to defaults?')) {
                settingsManager.reset();
            }
        });
    }
}

// ===== HAMBURGER MENU =====
const hamburger = document.getElementById('hamburger');
const navLinks = document.getElementById('navLinks');
const navOverlay = document.getElementById('navOverlay');

function toggleMenu() {
    hamburger.classList.toggle('active');
    navLinks.classList.toggle('active');
    navOverlay.classList.toggle('active');
    document.body.style.overflow = navLinks.classList.contains('active') ? 'hidden' : '';
}

if (hamburger) {
    hamburger.addEventListener('click', toggleMenu);
}

if (navOverlay) {
    navOverlay.addEventListener('click', toggleMenu);
}

document.querySelectorAll('.nav-links a').forEach(link => {
    link.addEventListener('click', () => {
        if (navLinks && navLinks.classList.contains('active')) {
            toggleMenu();
        }
    });
});

// ===== CHARTS INITIALIZATION =====
let powerChart = null;
let dailyChart = null;

const powerChartEl = document.getElementById('powerChart');
if (powerChartEl) {
    const ctx = powerChartEl.getContext('2d');

    // Create gradient
    const gradient = ctx.createLinearGradient(0, 0, 0, 400);
    gradient.addColorStop(0, 'rgba(76, 201, 240, 0.5)'); // Cyan
    gradient.addColorStop(1, 'rgba(67, 97, 238, 0.0)'); // Blue

    powerChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: [],
            datasets: [{
                label: 'Power (W)',
                data: [],
                borderColor: '#4CC9F0', // Electric Cyan
                backgroundColor: gradient,
                borderWidth: 2,
                tension: 0.4,
                fill: true,
                pointRadius: 0,
                pointHoverRadius: 6,
                pointBackgroundColor: '#4CC9F0',
                pointBorderColor: '#0B0E14',
                pointBorderWidth: 2
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction: {
                intersect: false,
                mode: 'index'
            },
            scales: {
                x: {
                    display: true,
                    grid: { color: 'rgba(255, 255, 255, 0.05)', drawBorder: false },
                    ticks: { color: '#94A3B8', maxRotation: 0, maxTicksLimit: 6 }
                },
                y: {
                    beginAtZero: true,
                    grid: { color: 'rgba(255, 255, 255, 0.05)', drawBorder: false },
                    ticks: { color: '#94A3B8' }
                }
            },
            plugins: {
                legend: { display: false },
                tooltip: {
                    backgroundColor: 'rgba(26, 31, 44, 0.9)',
                    titleColor: '#4CC9F0',
                    bodyColor: '#F0F0F0',
                    borderColor: 'rgba(76, 201, 240, 0.3)',
                    borderWidth: 1,
                    padding: 12,
                    displayColors: false
                }
            },
            animation: { duration: 0 }
        }
    });
}

const dailyChartEl = document.getElementById('dailyChart');
if (dailyChartEl) {
    const ctxDaily = dailyChartEl.getContext('2d');
    dailyChart = new Chart(ctxDaily, {
        type: 'bar',
        data: {
            labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            datasets: [{
                label: 'Usage (kWh)',
                data: [12.5, 19.2, 15.8, 17.3, 22.1, 18.6, 24.2],
                backgroundColor: 'rgba(0, 245, 212, 0.7)', // Neon Mint
                borderColor: '#00F5D4',
                borderWidth: 1,
                borderRadius: 4,
                hoverBackgroundColor: '#00F5D4'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                x: {
                    grid: { display: false },
                    ticks: { color: '#94A3B8' }
                },
                y: {
                    beginAtZero: true,
                    grid: { color: 'rgba(255, 255, 255, 0.05)', drawBorder: false },
                    ticks: { color: '#94A3B8' }
                }
            },
            plugins: {
                legend: { display: false },
                tooltip: {
                    backgroundColor: 'rgba(26, 31, 44, 0.9)',
                    titleColor: '#00F5D4',
                    bodyColor: '#F0F0F0',
                    borderColor: 'rgba(0, 245, 212, 0.3)',
                    borderWidth: 1
                }
            }
        }
    });
}

// ===== PIE CHART INITIALIZATION =====
let pieChart = null;
const pieChartEl = document.getElementById('pieChart');
if (pieChartEl) {
    const ctxPie = pieChartEl.getContext('2d');
    pieChart = new Chart(ctxPie, {
        type: 'doughnut',
        data: {
            labels: ['Normal', 'Theft Detected'],
            datasets: [{
                data: [100, 0], // Initial dummy data
                backgroundColor: ['#00F5D4', '#F72585'], // Neon Mint, Hyper-Pink
                borderWidth: 0,
                hoverOffset: 4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            cutout: '75%',
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: {
                        color: '#E0E6ED',
                        font: { family: "'Inter', sans-serif", size: 12 },
                        usePointStyle: true,
                        padding: 20
                    }
                },
                tooltip: {
                    backgroundColor: 'rgba(26, 31, 44, 0.9)',
                    bodyColor: '#E0E6ED',
                    borderColor: 'rgba(255, 255, 255, 0.1)',
                    borderWidth: 1
                }
            }
        }
    });
}

// ===== UTILITY FUNCTIONS =====
function formatTime(date) {
    return date.toLocaleTimeString('en-IN', {
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false
    });
}

function formatDateTime(timestamp) {
    const date = new Date(timestamp);
    return date.toLocaleString('en-IN', {
        day: '2-digit',
        month: 'short',
        hour: '2-digit',
        minute: '2-digit'
    });
}

// ===== DATA FETCHING =====
let totalEnergy = 0;
let lastTimestamp = null;

async function fetchEnergyData() {
    try {
        // CHANGED: Use the new Simple AppValue endpoint
        const response = await fetch('/dashboard/value');
        if (!response.ok) throw new Error('API not responding');

        const data = await response.json();
        const now = new Date();

        // Use API values directly if available, else calculate
        const voltage = data.voltage || 0;
        const current = data.current || 0;
        const power = data.power !== undefined ? data.power : (voltage * current);
        const energy = data.energy || 0; // Using API value for energy

        // Update reading values
        const voltageEl = document.getElementById('voltage-value');
        const currentEl = document.getElementById('current-value');
        const powerEl = document.getElementById('power-value');
        const energyEl = document.getElementById('energy-value');

        if (voltageEl) voltageEl.textContent = voltage.toFixed(1);
        if (currentEl) currentEl.textContent = current.toFixed(2);
        if (powerEl) powerEl.textContent = power.toFixed(1);
        if (energyEl) energyEl.textContent = energy.toFixed(2);

        // Update timestamps
        const timeStr = formatTime(now);
        ['voltage-time', 'current-time', 'power-time'].forEach(id => {
            const el = document.getElementById(id);
            if (el) el.textContent = timeStr;
        });

        // Update last update time
        const lastUpdateEl = document.getElementById('lastUpdate');
        if (lastUpdateEl) lastUpdateEl.textContent = timeStr;

        // Determine theft status based on thresholds (matching backend: voltage < 180 or current > 50)
        const theftDetected = voltage < 180 || current > 50;
        updateTheftStatus(theftDetected, current);

        // Update connection status
        updateConnectionStatus(true);

        if (powerChart) {
            const timeLabel = formatTime(now);
            if (powerChart.data.labels.length > 20) {
                powerChart.data.labels.shift();
                powerChart.data.datasets[0].data.shift();
            }
            powerChart.data.labels.push(timeLabel);
            powerChart.data.datasets[0].data.push(power);
            powerChart.update('none');
        }

        // Update Pie Chart if available (Simulated for now based on current status)
        if (pieChart) {
            // In a real scenario, this would come from an aggregate API
            // For demo: if theft detected, increase theft count?
            // Let's keep it static data for now unless we confirm the API.
        }

    } catch (error) {
        console.error('Error fetching data:', error);
        updateConnectionStatus(false);
    }
}

async function fetchDashboardSummary() {
    try {
        const response = await fetch('/api/dashboard/summary');
        if (!response.ok) return;
        const data = await response.json();

        if (pieChart) {
            // Update pie chart with Normal vs Theft counts
            const total = data.totalReadings || 100;
            const theft = data.theftDetectedCount || 0;
            const normal = total - theft;

            pieChart.data.datasets[0].data = [normal, theft];
            pieChart.update();
        }
    } catch (error) {
        console.error('Error fetching summary:', error);
    }
}

// ===== MANUAL CONTROL LOGIC =====
function initManualControls() {
    const updateForm = document.getElementById('updateValuesForm');
    if (!updateForm) return;

    updateForm.addEventListener('submit', async function (e) {
        e.preventDefault();

        const btn = document.getElementById('updateBtn');
        const statusEl = document.getElementById('updateStatus');

        if (btn) {
            btn.disabled = true;
            btn.textContent = 'Updating...';
        }

        const voltage = document.getElementById('update-voltage').value;
        const current = document.getElementById('update-current').value;
        const power = document.getElementById('update-power').value;
        const energy = document.getElementById('update-energy').value;

        try {
            const params = new URLSearchParams();
            if (voltage) params.append('voltage', voltage);
            if (current) params.append('current', current);
            if (power) params.append('power', power);
            if (energy) params.append('energy', energy);

            const response = await fetch(`/dashboard/update?${params.toString()}`, {
                method: 'POST'
            });

            if (response.ok) {
                const data = await response.json();
                if (statusEl) {
                    statusEl.textContent = `✓ Values updated! Voltage: ${data.voltage}V, Current: ${data.current}A`;
                    statusEl.className = 'success';
                    statusEl.style.color = 'var(--status-normal)';
                    statusEl.style.display = 'block';

                    // Clear form
                    updateForm.reset();

                    setTimeout(() => {
                        statusEl.className = '';
                        statusEl.textContent = '';
                        statusEl.style.display = 'none';
                    }, 3000);
                }
                // Fetch immediately to update UI
                fetchEnergyData();
            } else {
                throw new Error('Update failed');
            }
        } catch (error) {
            console.error('Error updating values:', error);
            if (statusEl) {
                statusEl.textContent = '✗ Failed to update values.';
                statusEl.className = 'error';
                statusEl.style.color = 'var(--status-critical)';
                statusEl.style.display = 'block';
            }
        } finally {
            if (btn) {
                btn.disabled = false;
                btn.textContent = 'Update Values';
            }
        }
    });
}

function updateTheftStatus(theftDetected, current) {
    const badge = document.getElementById('theftBadge');
    const statusText = document.getElementById('theftStatusText');
    const description = document.getElementById('theftDescription');

    if (!badge || !statusText || !description) return;

    const statusIndicator = badge.querySelector('.status-indicator');

    if (theftDetected) {
        badge.className = 'status-badge danger';
        if (statusIndicator) statusIndicator.className = 'status-indicator status-danger';
        statusText.textContent = 'THEFT DETECTED';
        description.textContent = `High current draw detected (${current.toFixed(2)}A). Possible unauthorized usage or bypass.`;
    } else if (current > 8) {
        badge.className = 'status-badge warning';
        if (statusIndicator) statusIndicator.className = 'status-indicator status-warning';
        statusText.textContent = 'SUSPICIOUS';
        description.textContent = 'Elevated current levels detected. Monitoring closely.';
    } else {
        badge.className = 'status-badge normal';
        if (statusIndicator) statusIndicator.className = 'status-indicator status-normal';
        statusText.textContent = 'SECURE';
        description.textContent = 'System is operating normally. No suspicious activity detected.';
    }
}

function updateConnectionStatus(isOnline) {
    // Landing Page Status
    const landingStatus = document.getElementById('systemStatus');
    const landingText = document.getElementById('statusText');

    if (landingStatus) {
        if (isOnline) {
            landingStatus.classList.remove('offline');
            if (landingText) landingText.textContent = 'System Online';
        } else {
            landingStatus.classList.add('offline');
            if (landingText) landingText.textContent = 'System Offline';
        }
    }

    // Dashboard Status
    const dashboardStatus = document.getElementById('connectionStatus');
    if (dashboardStatus) {
        const indicator = dashboardStatus.querySelector('.status-indicator');
        const text = dashboardStatus.querySelector('span:last-child');

        if (isOnline) {
            if (indicator) indicator.className = 'status-indicator status-normal';
            if (text) text.textContent = 'System Online';
        } else {
            if (indicator) indicator.className = 'status-indicator status-danger';
            if (text) text.textContent = 'Connection Lost';
        }
    }
}

// ===== ALERTS HANDLING =====
async function fetchDashboardAlerts() {
    try {
        const response = await fetch('/api/alerts/unresolved');
        // Fallback if endpoint differs, but dashboard.html used this.
        // app.js used /api/alerts. Let's try unresolved first, then /api/alerts?
        // Actually, dashboard.html logic was: /api/alerts/unresolved
        // app.js logic was: /api/alerts
        // Let's use /api/alerts/unresolved to match dashboard behavior for "Active Theft Alerts"
        // If that fails, we can catch it.
        // But wait, app.js previously used /api/alerts which might return ALL alerts.
        // "Active Theft Alerts" implies unresolved.

        let data;
        const resUnresolved = await fetch('/api/alerts/unresolved');
        if (resUnresolved.ok) {
            data = await resUnresolved.json();
        } else {
            const resAll = await fetch('/api/alerts');
            if (resAll.ok) {
                const all = await resAll.json();
                data = Array.isArray(all) ? all : (all.alertHistory || []);
                // Filter?
            }
        }

        if (!data) return;

        const container = document.getElementById('alertsList');
        // If container is missing, maybe we are on a page with table?
        const tableBody = document.getElementById('alertsTableBody');

        if (container) {
            // Dashboard view (divs)
            if (data.length === 0) {
                container.innerHTML = '<div class="no-alerts">No active alerts. System is secure.</div>';
                return;
            }

            container.innerHTML = data.map(alert => `
                <div class="alert-item" id="alert-${alert.id}">
                    <div class="alert-info">
                        <div class="alert-type">${alert.alertType || 'Alert'} - Meter: ${alert.meterId || 'N/A'}</div>
                        <div class="alert-desc">${alert.description || 'Suspicious activity detected'}</div>
                    </div>
                    <div class="alert-time">${formatDateTime(alert.timestamp)}</div>
                    <button class="resolve-btn" onclick="resolveAlert(${alert.id})">Resolve</button>
                </div>
            `).join('');
        } else if (tableBody) {
            // Alerts page view (table) - keep existing logic if needed
            // ...
        }

    } catch (error) {
        console.error('Error fetching alerts:', error);
    }
}

// ===== READINGS HANDLING =====
async function fetchReadings() {
    try {
        const response = await fetch('/api/dashboard/readings');
        if (!response.ok) return;

        const readings = await response.json();
        const tbody = document.getElementById('readingsBody');

        if (!tbody) return;

        if (readings.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; color: var(--text-muted);">No readings yet.</td></tr>';
            return;
        }

        // Update table (last 10)
        tbody.innerHTML = readings.slice(-10).reverse().map(r => `
            <tr>
                <td>${r.meterId || 'N/A'}</td>
                <td>${r.voltage?.toFixed(1) || '0'}</td>
                <td>${r.current?.toFixed(2) || '0'}</td>
                <td>${r.power?.toFixed(1) || '0'}</td>
                <td>${formatDateTime(r.timestamp)}</td>
                <td>
                    <span class="theft-badge ${r.theftDetected ? 'theft' : 'normal'}">
                        ${r.theftDetected ? 'THEFT' : 'Normal'}
                    </span>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Error fetching readings:', error);
    }
}

// Global resolve function
window.resolveAlert = async function (id) {
    try {
        const response = await fetch(`/api/alerts/${id}/resolve`, { method: 'PUT' });
        if (response && response.ok) {
            const el = document.getElementById(`alert-${id}`);
            if (el) el.remove();
            fetchDashboardAlerts();
            fetchDashboardSummary(); // Update charts too
        }
    } catch (error) {
        console.error('Error resolving alert:', error);
    }
};

// ===== ALERTS PAGE LOGIC =====
async function initAlertsPage() {
    const totalAlertsEl = document.getElementById('totalAlerts');
    // specific check for alerts page element
    if (!totalAlertsEl) return;

    // Re-use fetchDashboardAlerts logic but for full page
    async function fetchFullAlerts() {
        try {
            const response = await fetch('/api/alerts');
            const data = await response.json();

            // Handle both array responses and wrapper objects
            const alertsArray = Array.isArray(data) ? data : (data.alertHistory || []);

            // Update summary cards
            const totalAlerts = alertsArray.length;
            const activeTheftAlerts = alertsArray.filter(a => !a.resolved).length;
            if (document.getElementById('totalAlerts')) document.getElementById('totalAlerts').textContent = totalAlerts;
            if (document.getElementById('activeTheftAlerts')) document.getElementById('activeTheftAlerts').textContent = activeTheftAlerts;
            if (document.getElementById('systemHealth')) document.getElementById('systemHealth').textContent = (activeTheftAlerts > 0 ? 85 : 100) + '%';

            // Update current status badge (Big one on alerts page)
            const statusBadge = document.getElementById('currentStatusBadge');
            const statusText = document.getElementById('currentStatusText');
            const statusDesc = document.getElementById('currentStatusDesc');

            if (statusBadge) {
                const statusDot = statusBadge.querySelector('.status-indicator');

                if (data.activeTheftAlerts > 0) {
                    statusBadge.className = 'status-badge danger';
                    if (statusDot) statusDot.className = 'status-indicator status-danger';
                    if (statusText) statusText.textContent = 'THEFT DETECTED';
                    if (statusDesc) statusDesc.textContent = 'Suspicious activity detected! Immediate attention required.';
                } else if (data.totalAlerts > 0) {
                    statusBadge.className = 'status-badge warning';
                    if (statusDot) statusDot.className = 'status-indicator status-warning';
                    if (statusText) statusText.textContent = 'SUSPICIOUS';
                    if (statusDesc) statusDesc.textContent = 'Some anomalies detected. System monitoring closely.';
                } else {
                    statusBadge.className = 'status-badge normal';
                    if (statusDot) statusDot.className = 'status-indicator status-normal';
                    if (statusText) statusText.textContent = 'NORMAL';
                    if (statusDesc) statusDesc.textContent = 'All systems operating within normal parameters.';
                }
            }

            // Update alert history table (Alerts Page Specific Table)
            const tbody = document.getElementById('alertHistoryBody');
            if (tbody) {
                const alerts = alertsArray;
                if (alerts.length === 0) {
                    tbody.innerHTML = `
                        <tr>
                            <td colspan="5" style="text-align: center; color: var(--text-muted); padding: 2rem;">
                                No alerts recorded. System is operating normally.
                            </td>
                        </tr>
                    `;
                } else {
                    tbody.innerHTML = alerts.map(alert => `
                        <tr class="${alert.severity === 'CRITICAL' ? 'critical' : ''}">
                            <td>${formatDateTime(alert.timestamp)}</td>
                            <td>${alert.type}</td>
                            <td>${alert.value} ${alert.unit || 'W'}</td>
                            <td>${alert.status || 'Active'}</td>
                            <td>
                                <span class="severity-badge ${alert.severity.toLowerCase()}">
                                    ${alert.severity}
                                </span>
                            </td>
                        </tr>
                    `).join('');
                }
            }
        } catch (error) {
            console.error('Error fetching full alerts:', error);
        }
    }

    fetchFullAlerts();
    setInterval(fetchFullAlerts, 5000);
}

// ===== CONTACT PAGE LOGIC =====
function initContactPage() {
    const contactForm = document.getElementById('contactForm');
    if (!contactForm) return;

    contactForm.addEventListener('submit', function (e) {
        e.preventDefault();

        const statusEl = document.getElementById('formStatus');
        if (statusEl) {
            statusEl.style.display = 'block';
            statusEl.style.color = 'var(--secondary)';
            statusEl.textContent = 'Message sent successfully! (Demo mode)';

            setTimeout(() => {
                statusEl.style.display = 'none';
            }, 3000);
        }

        this.reset();
    });
}

// ===== LOAD CHART HISTORY =====
async function loadChartHistory() {
    try {
        const response = await fetch('/api/dashboard/readings');
        if (!response.ok) return;

        const readings = await response.json();
        if (!powerChart || readings.length === 0) return;

        // Sort by timestamp, take last 20
        const sorted = readings.sort((a, b) =>
            new Date(a.timestamp) - new Date(b.timestamp)
        ).slice(-20);

        powerChart.data.labels = sorted.map(r =>
            new Date(r.timestamp).toLocaleTimeString('en-IN', {
                hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: false
            })
        );
        powerChart.data.datasets[0].data = sorted.map(r => r.power);
        powerChart.update('none');
    } catch (error) {
        console.error('Error loading chart history:', error);
    }
}

// ===== INITIALIZATION =====
let energyRefreshInterval = null;
let alertsRefreshInterval = null;

window.updateRefreshInterval = function (ms) {
    if (energyRefreshInterval) clearInterval(energyRefreshInterval);
    energyRefreshInterval = setInterval(fetchEnergyData, ms);
};

function init() {
    // Initialize settings panel
    initSettingsPanel();

    // Start data polling with settings-based interval
    const voltageVal = document.getElementById('voltage-value');
    if (voltageVal) {
        // Only fetch energy data if widgets exist (Dashboard/Home)
        fetchEnergyData();
        loadChartHistory(); // Load historical readings for chart
        const refreshRate = settingsManager.get('autoRefresh') || 5000;
        energyRefreshInterval = setInterval(fetchEnergyData, refreshRate);
    }

    // Initialize Dashboard Alerts Widget
    const alertsContainer = document.getElementById('alertsList');
    if (alertsContainer) {
        fetchDashboardAlerts();
        alertsRefreshInterval = setInterval(fetchDashboardAlerts, 10000);
    }

    // Initialize Recent Readings
    const readingsTable = document.getElementById('readingsBody');
    if (readingsTable) {
        fetchReadings();
        setInterval(fetchReadings, 10000);
    }

    // Initialize Dashboard Summary (Pie Chart)
    if (document.getElementById('pieChart')) {
        fetchDashboardSummary();
        setInterval(fetchDashboardSummary, 10000);
    }

    // Page Specific Inits
    initAlertsPage();
    initContactPage();
    initManualControls();

    // Check system status (Landing Page specific pill)
    const systemStatusPill = document.getElementById('systemStatus');
    if (systemStatusPill) {
        if (!voltageVal) {
            async function checkSimplyStatus() {
                try {
                    const response = await fetch('/api/status');
                    const data = await response.json();
                    updateConnectionStatus(data.online);
                } catch (e) {
                    updateConnectionStatus(false);
                }
            }
            checkSimplyStatus();
            setInterval(checkSimplyStatus, 10000);
        }
    }
}

// Run when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
} else {
    init();
}
