#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
INPUT_MD="${ROOT_DIR}/docs/PROJECT_FLOWCHART.md"
OUTPUT_HTML="${ROOT_DIR}/docs/flowchart.html"

mkdir -p "${ROOT_DIR}/docs"

awk -v OUTPUT_HTML="${OUTPUT_HTML}" '
  function esc_html(s,    t) {
    t = s
    gsub(/&/, "\\&amp;", t)
    gsub(/</, "\\&lt;", t)
    gsub(/>/, "\\&gt;", t)
    return t
  }

  # Normalize Windows CRLF -> LF so we never emit raw \r into JS string literals
  # (a raw carriage return inside a quoted JS string is a syntax error).
  { sub(/\r$/, "", $0) }

  BEGIN {
    heading = "Flowchart"
    in_mermaid = 0
    block = ""
    block_count = 0

    print "<!doctype html>" > OUTPUT_HTML
    print "<html lang=\"en\">" >> OUTPUT_HTML
    print "<head>" >> OUTPUT_HTML
    print "  <meta charset=\"utf-8\" />" >> OUTPUT_HTML
    print "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />" >> OUTPUT_HTML
    print "  <title>DuckRun – Project Flowchart</title>" >> OUTPUT_HTML
    print "  <style>" >> OUTPUT_HTML
    print "    :root{--bg:#0b1220;--panel:#0f1a2b;--panel2:#0c1526;--text:#e5e7eb;--muted:#9ca3af;--border:#1f2a44;--accent:#60a5fa;--accent2:#34d399;--danger:#f87171}" >> OUTPUT_HTML
    print "    html,body{height:100%}" >> OUTPUT_HTML
    print "    body{margin:0;background:radial-gradient(1200px 800px at 15% 10%, #13284a 0%, var(--bg) 50%);color:var(--text);font-family:ui-sans-serif,system-ui,-apple-system,Segoe UI,Roboto,Helvetica,Arial,\"Apple Color Emoji\",\"Segoe UI Emoji\"}" >> OUTPUT_HTML
    print "    a{color:inherit}" >> OUTPUT_HTML
    print "    .app{display:grid;grid-template-rows:auto auto 1fr;min-height:100%}" >> OUTPUT_HTML
    print "    header{display:flex;align-items:center;justify-content:space-between;gap:12px;padding:16px 18px;border-bottom:1px solid var(--border);background:linear-gradient(180deg,var(--panel),transparent)}" >> OUTPUT_HTML
    print "    .title{display:flex;flex-direction:column;gap:2px}" >> OUTPUT_HTML
    print "    .title h1{margin:0;font-size:16px;letter-spacing:.2px}" >> OUTPUT_HTML
    print "    .title p{margin:0;color:var(--muted);font-size:12px}" >> OUTPUT_HTML
    print "    .actions{display:flex;align-items:center;gap:8px;flex-wrap:wrap;justify-content:flex-end}" >> OUTPUT_HTML
    print "    button{appearance:none;border:1px solid var(--border);background:linear-gradient(180deg,#14233a,#0f1b31);color:var(--text);padding:8px 10px;border-radius:10px;font-size:12px;cursor:pointer}" >> OUTPUT_HTML
    print "    button:hover{border-color:#2a3a63}" >> OUTPUT_HTML
    print "    button:active{transform:translateY(1px)}" >> OUTPUT_HTML
    print "    button.primary{border-color:rgba(96,165,250,.45);box-shadow:0 0 0 2px rgba(96,165,250,.08) inset}" >> OUTPUT_HTML
    print "    button.good{border-color:rgba(52,211,153,.45);box-shadow:0 0 0 2px rgba(52,211,153,.08) inset}" >> OUTPUT_HTML
    print "    button.danger{border-color:rgba(248,113,113,.45);box-shadow:0 0 0 2px rgba(248,113,113,.08) inset}" >> OUTPUT_HTML
    print "    .tabs{display:flex;gap:8px;align-items:center;padding:10px 18px;border-bottom:1px solid var(--border);background:rgba(10,18,34,.55);backdrop-filter:blur(8px)}" >> OUTPUT_HTML
    print "    .tab{display:flex;align-items:center;gap:8px;padding:8px 10px;border:1px solid var(--border);border-radius:12px;background:rgba(15,26,43,.45);cursor:pointer;font-size:12px;color:var(--muted)}" >> OUTPUT_HTML
    print "    .tab[aria-selected=\"true\"]{color:var(--text);border-color:rgba(96,165,250,.55);box-shadow:0 0 0 2px rgba(96,165,250,.10) inset}" >> OUTPUT_HTML
    print "    .tab small{opacity:.75}" >> OUTPUT_HTML
    print "    main{display:grid;grid-template-columns:1fr;min-height:0}" >> OUTPUT_HTML
    print "    .viewer{position:relative;min-height:0}" >> OUTPUT_HTML
    print "    .panel{height:100%;min-height:0;border-top:0}" >> OUTPUT_HTML
    print "    .diagram{display:none;height:100%;min-height:0}" >> OUTPUT_HTML
    print "    .diagram.active{display:block}" >> OUTPUT_HTML
    print "    .svgWrap{height:calc(100vh - 132px);min-height:520px;background:linear-gradient(180deg,rgba(15,26,43,.75),rgba(12,21,38,.75));border:1px solid var(--border);border-radius:16px;margin:18px;overflow:hidden;position:relative}" >> OUTPUT_HTML
    print "    .svgWrap .hint{position:absolute;right:14px;top:12px;font-size:11px;color:var(--muted);background:rgba(10,18,34,.6);border:1px solid var(--border);padding:6px 8px;border-radius:999px}" >> OUTPUT_HTML
    print "    .svgWrap svg{width:100%;height:100%}" >> OUTPUT_HTML
    print "    .toast{position:fixed;left:50%;bottom:16px;transform:translateX(-50%);background:rgba(10,18,34,.9);border:1px solid var(--border);padding:10px 12px;border-radius:12px;color:var(--text);font-size:12px;display:none;max-width:min(900px,calc(100vw - 28px))}" >> OUTPUT_HTML
    print "    .toast.show{display:block}" >> OUTPUT_HTML
    print "    code.kbd{font-family:ui-monospace,SFMono-Regular,Menlo,Monaco,Consolas,monospace;background:rgba(255,255,255,.06);border:1px solid rgba(255,255,255,.08);padding:1px 6px;border-radius:8px;font-size:11px}" >> OUTPUT_HTML
    print "  </style>" >> OUTPUT_HTML
    print "</head>" >> OUTPUT_HTML
    print "<body>" >> OUTPUT_HTML
    print "  <div class=\"app\">" >> OUTPUT_HTML
    print "    <header>" >> OUTPUT_HTML
    print "      <div class=\"title\">" >> OUTPUT_HTML
    print "        <h1>DuckRun – Interactive Project Flowchart</h1>" >> OUTPUT_HTML
    print "        <p>Pan/zoom, switch diagrams, export SVG. Generated from <code class=\"kbd\">PROJECT_FLOWCHART.md</code>.</p>" >> OUTPUT_HTML
    print "      </div>" >> OUTPUT_HTML
    print "      <div class=\"actions\">" >> OUTPUT_HTML
    print "        <button id=\"zoomIn\" class=\"primary\" title=\"Zoom in\">Zoom In</button>" >> OUTPUT_HTML
    print "        <button id=\"zoomOut\" class=\"primary\" title=\"Zoom out\">Zoom Out</button>" >> OUTPUT_HTML
    print "        <button id=\"reset\" class=\"good\" title=\"Fit + center\">Reset View</button>" >> OUTPUT_HTML
    print "        <button id=\"export\" title=\"Download current diagram as SVG\">Download SVG</button>" >> OUTPUT_HTML
    print "      </div>" >> OUTPUT_HTML
    print "    </header>" >> OUTPUT_HTML
    print "    <nav class=\"tabs\" id=\"tabs\" aria-label=\"Flowchart tabs\"></nav>" >> OUTPUT_HTML
    print "    <main>" >> OUTPUT_HTML
    print "      <div class=\"viewer panel\" id=\"viewer\"></div>" >> OUTPUT_HTML
    print "    </main>" >> OUTPUT_HTML
    print "  </div>" >> OUTPUT_HTML
    print "  <div class=\"toast\" id=\"toast\"></div>" >> OUTPUT_HTML
    print "" >> OUTPUT_HTML
    print "  <script src=\"https://cdn.jsdelivr.net/npm/mermaid@10/dist/mermaid.min.js\"></script>" >> OUTPUT_HTML
    print "  <script src=\"https://cdn.jsdelivr.net/npm/svg-pan-zoom@3.6.1/dist/svg-pan-zoom.min.js\"></script>" >> OUTPUT_HTML
    print "  <script>" >> OUTPUT_HTML
    print "    const DIAGRAMS = [];" >> OUTPUT_HTML
  }

  /^##[[:space:]]+/ {
    heading = substr($0, 4)
    next
  }

  /^```mermaid[[:space:]]*$/ {
    in_mermaid = 1
    block = ""
    next
  }

  /^```[[:space:]]*$/ && in_mermaid == 1 {
    in_mermaid = 0
    block_count++
    title = heading
    # Escape quotes for JS string literal
    gsub(/"/, "\\\"", title)
    
    # Escape special characters for JS template literal in block
    gsub(/\\/, "\\\\", block)
    gsub(/`/, "\\`", block)
    gsub(/\$/, "\\$", block)

    print "    DIAGRAMS.push({ title: \"" title "\", code: `" block "` });" >> OUTPUT_HTML
    next
  }

  {
    if (in_mermaid == 1) {
      block = block $0 "\n"
    }
  }

  END {
    print "" >> OUTPUT_HTML
    print "    mermaid.initialize({" >> OUTPUT_HTML
    print "      startOnLoad: false," >> OUTPUT_HTML
    print "      securityLevel: \"loose\"," >> OUTPUT_HTML
    print "      theme: \"dark\"," >> OUTPUT_HTML
    print "      themeVariables: {" >> OUTPUT_HTML
    print "        fontFamily: \"ui-sans-serif,system-ui,-apple-system,Segoe UI,Roboto,Helvetica,Arial\"," >> OUTPUT_HTML
    print "        primaryColor: \"#0f1a2b\"," >> OUTPUT_HTML
    print "        primaryBorderColor: \"#1f2a44\"," >> OUTPUT_HTML
    print "        primaryTextColor: \"#e5e7eb\"," >> OUTPUT_HTML
    print "        lineColor: \"#93c5fd\"," >> OUTPUT_HTML
    print "        edgeLabelBackground: \"#0b1220\"" >> OUTPUT_HTML
    print "      }," >> OUTPUT_HTML
    # Use pure SVG text labels to avoid foreignObject/HTML label clipping (last character cut off).
    print "      flowchart: { curve: \"basis\", padding: 8, nodeSpacing: 34, rankSpacing: 44, htmlLabels: false }" >> OUTPUT_HTML
    print "    });" >> OUTPUT_HTML
    print "" >> OUTPUT_HTML
    print "    const tabsEl = document.getElementById(\"tabs\");" >> OUTPUT_HTML
    print "    const viewerEl = document.getElementById(\"viewer\");" >> OUTPUT_HTML
    print "    const toastEl = document.getElementById(\"toast\");" >> OUTPUT_HTML
    print "    const panZoomInstances = new Map();" >> OUTPUT_HTML
    print "    let activeIndex = 0;" >> OUTPUT_HTML
    print "" >> OUTPUT_HTML
    print "    function toast(msg) {" >> OUTPUT_HTML
    print "      toastEl.textContent = msg;" >> OUTPUT_HTML
    print "      toastEl.classList.add(\"show\");" >> OUTPUT_HTML
    print "      window.clearTimeout(toast.__t);" >> OUTPUT_HTML
    print "      toast.__t = window.setTimeout(() => toastEl.classList.remove(\"show\"), 2200);" >> OUTPUT_HTML
    print "    }" >> OUTPUT_HTML
    print "" >> OUTPUT_HTML
    print "    function setActive(idx) {" >> OUTPUT_HTML
    print "      activeIndex = idx;" >> OUTPUT_HTML
    print "      [...tabsEl.querySelectorAll(\".tab\")].forEach((t, i) => t.setAttribute(\"aria-selected\", i === idx ? \"true\" : \"false\"));" >> OUTPUT_HTML
    print "      [...viewerEl.querySelectorAll(\".diagram\")].forEach((p, i) => p.classList.toggle(\"active\", i === idx));" >> OUTPUT_HTML
    print "      ensurePanZoom(idx);" >> OUTPUT_HTML
    print "      const wrap = viewerEl.querySelector(`.diagram[data-idx=\\\"${idx}\\\"] .svgWrap`);" >> OUTPUT_HTML
    print "      const pz = wrap ? panZoomInstances.get(wrap) : null;" >> OUTPUT_HTML
    print "      if (pz) { pz.resize(); pz.fit(); pz.center(); }" >> OUTPUT_HTML
    print "    }" >> OUTPUT_HTML
    print "" >> OUTPUT_HTML
    print "    function ensurePanZoom(idx) {" >> OUTPUT_HTML
    print "      const wrap = viewerEl.querySelector(`.diagram[data-idx=\\\"${idx}\\\"] .svgWrap`);" >> OUTPUT_HTML
    print "      if (!wrap || panZoomInstances.has(wrap)) return;" >> OUTPUT_HTML
    print "      const svgEl = wrap.querySelector(\"svg\");" >> OUTPUT_HTML
    print "      if (!svgEl) return;" >> OUTPUT_HTML
    print "      svgEl.style.height = \"100%\";" >> OUTPUT_HTML
    print "      svgEl.style.width = \"100%\";" >> OUTPUT_HTML
    print "      const pz = svgPanZoom(svgEl, {" >> OUTPUT_HTML
    print "        zoomEnabled: true," >> OUTPUT_HTML
    print "        controlIconsEnabled: false," >> OUTPUT_HTML
    print "        fit: true," >> OUTPUT_HTML
    print "        center: true," >> OUTPUT_HTML
    print "        minZoom: 0.15," >> OUTPUT_HTML
    print "        maxZoom: 30," >> OUTPUT_HTML
    print "        zoomScaleSensitivity: 0.2," >> OUTPUT_HTML
    print "        dblClickZoomEnabled: false," >> OUTPUT_HTML
    print "      });" >> OUTPUT_HTML
    print "      panZoomInstances.set(wrap, pz);" >> OUTPUT_HTML
    print "    }" >> OUTPUT_HTML
    print "" >> OUTPUT_HTML
    print "    function currentPanZoom() {" >> OUTPUT_HTML
    print "      const wrap = viewerEl.querySelector(`.diagram[data-idx=\\\"${activeIndex}\\\"] .svgWrap`);" >> OUTPUT_HTML
    print "      return panZoomInstances.get(wrap) || null;" >> OUTPUT_HTML
    print "    }" >> OUTPUT_HTML
    print "" >> OUTPUT_HTML
    print "    async function renderAll() {" >> OUTPUT_HTML
    print "      if (!Array.isArray(DIAGRAMS) || DIAGRAMS.length === 0) {" >> OUTPUT_HTML
    print "        viewerEl.innerHTML = \"<div style=\\\"padding:18px;color:#9ca3af\\\">No Mermaid blocks found in PROJECT_FLOWCHART.md.</div>\";" >> OUTPUT_HTML
    print "        return;" >> OUTPUT_HTML
    print "      }" >> OUTPUT_HTML
    print "" >> OUTPUT_HTML
    print "      tabsEl.innerHTML = \"\";" >> OUTPUT_HTML
    print "      viewerEl.innerHTML = \"\";" >> OUTPUT_HTML
    print "" >> OUTPUT_HTML
    print "      for (let i = 0; i < DIAGRAMS.length; i++) {" >> OUTPUT_HTML
    print "        const d = DIAGRAMS[i];" >> OUTPUT_HTML
    print "        const tab = document.createElement(\"button\");" >> OUTPUT_HTML
    print "        tab.className = \"tab\";" >> OUTPUT_HTML
    print "        tab.type = \"button\";" >> OUTPUT_HTML
    print "        tab.setAttribute(\"aria-selected\", i === 0 ? \"true\" : \"false\");" >> OUTPUT_HTML
    print "        tab.innerHTML = `${d.title} <small>#${i + 1}</small>`;" >> OUTPUT_HTML
    print "        tab.addEventListener(\"click\", () => setActive(i));" >> OUTPUT_HTML
    print "        tabsEl.appendChild(tab);" >> OUTPUT_HTML
    print "" >> OUTPUT_HTML
    print "        const panel = document.createElement(\"section\");" >> OUTPUT_HTML
    print "        panel.className = \"diagram\" + (i === 0 ? \" active\" : \"\");" >> OUTPUT_HTML
    print "        panel.dataset.idx = String(i);" >> OUTPUT_HTML
    print "        const wrap = document.createElement(\"div\");" >> OUTPUT_HTML
    print "        wrap.className = \"svgWrap\";" >> OUTPUT_HTML
    print "        wrap.innerHTML = `<div class=\\\"hint\\\">Drag to pan • Mousewheel to zoom • Double-click to zoom</div>`;" >> OUTPUT_HTML
    print "        panel.appendChild(wrap);" >> OUTPUT_HTML
    print "        viewerEl.appendChild(panel);" >> OUTPUT_HTML
    print "" >> OUTPUT_HTML
    print "        const id = `duckrun_mermaid_${i}`;" >> OUTPUT_HTML
    print "        try {" >> OUTPUT_HTML
    print "          const { svg } = await mermaid.render(id, d.code);" >> OUTPUT_HTML
    print "          const holder = document.createElement(\"div\");" >> OUTPUT_HTML
    print "          holder.innerHTML = svg;" >> OUTPUT_HTML
    print "          wrap.appendChild(holder.firstElementChild);" >> OUTPUT_HTML
    print "" >> OUTPUT_HTML
    print "          const svgEl = wrap.querySelector(\"svg\");" >> OUTPUT_HTML
    print "          if (svgEl) {" >> OUTPUT_HTML
    print "            svgEl.addEventListener(\"dblclick\", (e) => { e.preventDefault(); const pz = panZoomInstances.get(wrap); if (pz) pz.zoomIn(); });" >> OUTPUT_HTML
    print "            svgEl.addEventListener(\"click\", (evt) => {" >> OUTPUT_HTML
    print "              const t = evt.target;" >> OUTPUT_HTML
    print "              if (!t) return;" >> OUTPUT_HTML
    print "              const textEl = t.closest && t.closest(\"text\");" >> OUTPUT_HTML
    print "              if (textEl && textEl.textContent) toast(textEl.textContent.trim());" >> OUTPUT_HTML
    print "            });" >> OUTPUT_HTML
    print "          }" >> OUTPUT_HTML
    print "        } catch (err) {" >> OUTPUT_HTML
    print "          console.error(\"Mermaid render failed for diagram\", i, err);" >> OUTPUT_HTML
    print "          wrap.innerHTML = `<div style=\\\"padding:18px;color:#fca5a5\\\">Render failed for \"${d.title}\":<br><pre style=\\\"font-size:11px;white-space:pre-wrap\\\">${esc_html(String(err))}</pre></div>`;" >> OUTPUT_HTML
    print "        }" >> OUTPUT_HTML
    print "" >> OUTPUT_HTML
    print "      }" >> OUTPUT_HTML
    print "" >> OUTPUT_HTML
    print "      setActive(0);" >> OUTPUT_HTML
    print "    }" >> OUTPUT_HTML
    print "" >> OUTPUT_HTML
    print "    function downloadCurrentSvg() {" >> OUTPUT_HTML
    print "      const wrap = viewerEl.querySelector(`.diagram[data-idx=\\\"${activeIndex}\\\"] .svgWrap`);" >> OUTPUT_HTML
    print "      const svg = wrap && wrap.querySelector(\"svg\");" >> OUTPUT_HTML
    print "      if (!svg) return;" >> OUTPUT_HTML
    print "      const serializer = new XMLSerializer();" >> OUTPUT_HTML
    print "      const svgText = serializer.serializeToString(svg);" >> OUTPUT_HTML
    print "      const blob = new Blob([svgText], { type: \"image/svg+xml;charset=utf-8\" });" >> OUTPUT_HTML
    print "      const url = URL.createObjectURL(blob);" >> OUTPUT_HTML
    print "      const a = document.createElement(\"a\");" >> OUTPUT_HTML
    print "      a.href = url;" >> OUTPUT_HTML
    print "      const safeTitle = (DIAGRAMS[activeIndex]?.title || \"flowchart\").replace(/[^a-z0-9-_]+/gi, \"_\");" >> OUTPUT_HTML
    print "      a.download = `DuckRun_${safeTitle}.svg`;" >> OUTPUT_HTML
    print "      document.body.appendChild(a);" >> OUTPUT_HTML
    print "      a.click();" >> OUTPUT_HTML
    print "      a.remove();" >> OUTPUT_HTML
    print "      URL.revokeObjectURL(url);" >> OUTPUT_HTML
    print "      toast(\"Downloaded SVG\");" >> OUTPUT_HTML
    print "    }" >> OUTPUT_HTML
    print "" >> OUTPUT_HTML
    print "    document.getElementById(\"zoomIn\").addEventListener(\"click\", () => { const pz = currentPanZoom(); if (pz) pz.zoomIn(); });" >> OUTPUT_HTML
    print "    document.getElementById(\"zoomOut\").addEventListener(\"click\", () => { const pz = currentPanZoom(); if (pz) pz.zoomOut(); });" >> OUTPUT_HTML
    print "    document.getElementById(\"reset\").addEventListener(\"click\", () => { const pz = currentPanZoom(); if (pz) { pz.resetZoom(); pz.fit(); pz.center(); } });" >> OUTPUT_HTML
    print "    document.getElementById(\"export\").addEventListener(\"click\", downloadCurrentSvg);" >> OUTPUT_HTML
    print "" >> OUTPUT_HTML
    print "    renderAll().catch((e) => {" >> OUTPUT_HTML
    print "      console.error(e);" >> OUTPUT_HTML
    print "      viewerEl.innerHTML = `<div style=\\\"padding:18px;color:#fca5a5\\\">Critical failure: ${String(e)}</div>`;" >> OUTPUT_HTML
    print "    });" >> OUTPUT_HTML
    print "  </script>" >> OUTPUT_HTML
    print "</body>" >> OUTPUT_HTML
    print "</html>" >> OUTPUT_HTML
  }
' "${INPUT_MD}"

echo "Wrote: ${OUTPUT_HTML}"
