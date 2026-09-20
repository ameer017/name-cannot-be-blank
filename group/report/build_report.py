#!/usr/bin/env python3
"""Build a 2-3 page assignment report as a Word document (Google Docs importable)."""

from pathlib import Path

from docx import Document
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.enum.text import WD_ALIGN_PARAGRAPH, WD_LINE_SPACING
from docx.oxml import OxmlElement
from docx.oxml.ns import qn
from docx.shared import Cm, Inches, Pt, RGBColor
from PIL import Image

ROOT = Path(__file__).resolve().parent
SHOTS = ROOT / "screenshots"
OUTPUT = ROOT / "Group_Project_Management_System_Report.docx"


def flatten_black_background(src: Path, dest: Path) -> None:
    image = Image.open(src).convert("RGBA")
    pixels = image.load()
    width, height = image.size
    min_x, min_y, max_x, max_y = width, height, 0, 0
    for y in range(height):
        for x in range(width):
            r, g, b, a = pixels[x, y]
            if r < 20 and g < 20 and b < 20:
                pixels[x, y] = (255, 255, 255, 255)
            else:
                min_x = min(min_x, x)
                min_y = min(min_y, y)
                max_x = max(max_x, x)
                max_y = max(max_y, y)
    pad = 12
    crop = image.crop(
        (
            max(0, min_x - pad),
            max(0, min_y - pad),
            min(width, max_x + pad),
            min(height, max_y + pad),
        )
    )
    background = Image.new("RGB", crop.size, (255, 255, 255))
    background.paste(crop, mask=crop.split()[-1])
    background.save(dest)


def set_run_font(run, name="Times New Roman", size=12, bold=False, italic=False):
    run.font.name = name
    run._element.rPr.rFonts.set(qn("w:eastAsia"), name)
    run.font.size = Pt(size)
    run.bold = bold
    run.italic = italic
    run.font.color.rgb = RGBColor(0, 0, 0)


def add_paragraph(doc, text, *, size=12, bold=False, italic=False, align="left", space_after=6, space_before=0, first_line=True):
    para = doc.add_paragraph()
    para.alignment = {
        "left": WD_ALIGN_PARAGRAPH.LEFT,
        "center": WD_ALIGN_PARAGRAPH.CENTER,
        "justify": WD_ALIGN_PARAGRAPH.JUSTIFY,
        "right": WD_ALIGN_PARAGRAPH.RIGHT,
    }[align]
    pf = para.paragraph_format
    pf.space_after = Pt(space_after)
    pf.space_before = Pt(space_before)
    pf.line_spacing_rule = WD_LINE_SPACING.SINGLE
    if first_line and align == "justify":
        pf.first_line_indent = Cm(1.0)
    run = para.add_run(text)
    set_run_font(run, size=size, bold=bold, italic=italic)
    return para


def add_heading_styled(doc, text, size=13):
    para = doc.add_paragraph()
    para.alignment = WD_ALIGN_PARAGRAPH.LEFT
    para.paragraph_format.space_before = Pt(10)
    para.paragraph_format.space_after = Pt(6)
    run = para.add_run(text.upper())
    set_run_font(run, size=size, bold=True)
    return para


def shade_cell(cell, hex_color):
    tc = cell._tePr if hasattr(cell, "_tePr") else cell._tc
    tc_pr = tc.get_or_add_tcPr()
    shd = OxmlElement("w:shd")
    shd.set(qn("w:fill"), hex_color)
    shd.set(qn("w:val"), "clear")
    tc_pr.append(shd)


def set_cell_border(cell):
    tc = cell._tc
    tc_pr = tc.get_or_add_tcPr()
    tc_borders = OxmlElement("w:tcBorders")
    for edge in ("top", "left", "bottom", "right"):
        element = OxmlElement(f"w:{edge}")
        element.set(qn("w:val"), "single")
        element.set(qn("w:sz"), "4")
        element.set(qn("w:color"), "666666")
        tc_borders.append(element)
    tc_pr.append(tc_borders)


def set_cell_text(cell, text, *, bold=False, size=9, center=False):
    cell.text = ""
    para = cell.paragraphs[0]
    para.alignment = WD_ALIGN_PARAGRAPH.CENTER if center else WD_ALIGN_PARAGRAPH.LEFT
    para.paragraph_format.space_after = Pt(0)
    para.paragraph_format.space_before = Pt(1)
    run = para.add_run(text)
    set_run_font(run, size=size, bold=bold)


def add_caption(doc, text):
    para = doc.add_paragraph()
    para.alignment = WD_ALIGN_PARAGRAPH.CENTER
    para.paragraph_format.space_before = Pt(2)
    para.paragraph_format.space_after = Pt(8)
    run = para.add_run(text)
    set_run_font(run, size=10, italic=True)


def add_picture_centered(doc, path, width_inches):
    para = doc.add_paragraph()
    para.alignment = WD_ALIGN_PARAGRAPH.CENTER
    para.paragraph_format.space_after = Pt(0)
    para.paragraph_format.space_before = Pt(4)
    run = para.add_run()
    run.add_picture(str(path), width=Inches(width_inches))


def add_two_images(doc, left_path, right_path, width=3.15):
    table = doc.add_table(rows=1, cols=2)
    table.alignment = WD_TABLE_ALIGNMENT.CENTER
    table.autofit = True
    for idx, path in enumerate((left_path, right_path)):
        cell = table.rows[0].cells[idx]
        cell.text = ""
        para = cell.paragraphs[0]
        para.alignment = WD_ALIGN_PARAGRAPH.CENTER
        run = para.add_run()
        run.add_picture(str(path), width=Inches(width))
    return table


def prevent_table_autofit_overflow(table):
    tbl = table._tbl
    tbl_pr = tbl.tblPr
    tbl_w = OxmlElement("w:tblW")
    tbl_w.set(qn("w:w"), "5000")
    tbl_w.set(qn("w:type"), "pct")
    tbl_pr.append(tbl_w)


def build():
    diagram_src = SHOTS / "00-class-diagram.png"
    diagram_white = SHOTS / "00-class-diagram-white.png"
    flatten_black_background(diagram_src, diagram_white)

    doc = Document()
    section = doc.sections[0]
    section.page_width = Inches(8.5)
    section.page_height = Inches(11)
    section.left_margin = Inches(0.9)
    section.right_margin = Inches(0.9)
    section.top_margin = Inches(0.75)
    section.bottom_margin = Inches(0.75)

    style = doc.styles["Normal"]
    style.font.name = "Times New Roman"
    style.font.size = Pt(12)
    style.paragraph_format.space_after = Pt(6)

    add_paragraph(doc, "AHMADU BELLO UNIVERSITY, ZARIA", size=12, bold=True, align="center", first_line=False, space_after=0)
    add_paragraph(doc, "Department of Computer Science", size=12, align="center", first_line=False, space_after=0)
    add_paragraph(doc, "Object-Oriented Programming (Java)", size=12, align="center", first_line=False, space_after=8)
    add_paragraph(doc, "Group Project Management System", size=16, bold=True, align="center", first_line=False, space_after=2)
    add_paragraph(doc, "Short Design and Implementation Report", size=12, italic=True, align="center", first_line=False, space_after=8)
    add_paragraph(
        doc,
        "Group Members: IBRAHIM Amiruddeen  •  DANIEL OTU Courage Daniel  •  GO'AR Ritmunu Miracle",
        size=10,
        align="center",
        first_line=False,
        space_after=0,
    )
    add_paragraph(
        doc,
        "Instructor: Aliyu Garba (algarba@abu.edu.ng)",
        size=10,
        align="center",
        first_line=False,
        space_after=10,
    )

    add_heading_styled(doc, "1. Design Explanation")
    add_paragraph(
        doc,
        "The Group Project Management System is a desktop application built with Java Swing and AWT. "
        "It helps a course instructor keep student records, organise students into project groups, assign "
        "responsibilities, and generate progress reports. All application state is held in memory with "
        "standard Java collections (ArrayList and List); there is no external database.",
        align="justify",
        space_after=6,
    )
    add_paragraph(
        doc,
        "The design follows the Model-View-Controller (MVC) pattern so that data, presentation, and "
        "application startup remain separated.",
        align="justify",
        space_after=4,
        first_line=True,
    )

    bullets = [
        "Model (model package): Student, Group, and Responsibility encapsulate domain data. DataStore is the single in-memory repository and also loads seed records for demonstration.",
        "View (view package): MainFrame hosts a JTabbedPane with StudentsGroupsPanel, TasksPanel, and ReportsPanel. StudentDialog is a modal form for creating a student.",
        "Controller (controller package): AppController starts the user interface on the Event Dispatch Thread through SwingUtilities.invokeLater. User actions are handled by listeners in the view classes, which update DataStore and then refresh the visible widgets.",
    ]
    for item in bullets:
        para = doc.add_paragraph(style=None)
        para.paragraph_format.left_indent = Cm(0.75)
        para.paragraph_format.first_line_indent = Cm(-0.4)
        para.paragraph_format.space_after = Pt(3)
        para.paragraph_format.space_before = Pt(0)
        run = para.add_run("•  " + item)
        set_run_font(run, size=11)

    add_paragraph(
        doc,
        "MainFrame owns one DataStore instance and injects it into every panel. That shared store is what "
        "keeps the three tabs consistent. A tab-change listener calls refreshData() so that assigning a "
        "student to a group on the first tab immediately appears in the task combo boxes. Group.addMember() "
        "and removeMember() also write Student.groupId, enforcing the rule that a student belongs to only "
        "one group at a time.",
        align="justify",
        space_after=6,
        first_line=True,
    )

    add_picture_centered(doc, diagram_white, 6.5)
    add_caption(doc, "Figure 1. MVC structure and class relationships.")

    add_heading_styled(doc, "2. GUI Components Used")
    add_paragraph(
        doc,
        "The interface uses 14 Swing/AWT components and four layout managers (BorderLayout, GridLayout, "
        "FlowLayout, and GridBagLayout). The table below maps each component to the screen where it appears.",
        align="justify",
        space_after=6,
    )

    rows = [
        ("Component", "Where it appears", "Role"),
        ("JFrame", "MainFrame", "Top-level window"),
        ("JTabbedPane", "MainFrame", "Students & Groups, Tasks, Reports"),
        ("JMenuBar / JMenu / JMenuItem", "MainFrame", "File → Exit and Help → About"),
        ("JSplitPane", "StudentsGroupsPanel", "Resizable Students | Groups divider"),
        ("JTable", "Students & Groups, Tasks", "Student records and task list"),
        ("JList", "StudentsGroupsPanel", "Single-selection group list"),
        ("JTextArea", "Groups panel, Reports", "Member details and formatted reports"),
        ("JComboBox", "TasksPanel", "Group, assignee, and status selection"),
        ("JDialog / JTextField", "StudentDialog", "Modal ID, name, and email form"),
        ("JButton / JLabel / JPanel / JScrollPane", "All views", "Actions, labels, grouping, scrolling"),
    ]
    table = doc.add_table(rows=len(rows), cols=3)
    table.style = "Table Grid"
    prevent_table_autofit_overflow(table)
    for r, row in enumerate(rows):
        for c, value in enumerate(row):
            cell = table.rows[r].cells[c]
            set_cell_text(cell, value, bold=(r == 0), size=9)
            set_cell_border(cell)
            if r == 0:
                shade_cell(cell, "1F4E79")
                for p in cell.paragraphs:
                    for run in p.runs:
                        run.font.color.rgb = RGBColor(255, 255, 255)
                        run.bold = True
            elif r % 2 == 0:
                shade_cell(cell, "F2F2F2")
    widths = (Inches(2.2), Inches(2.3), Inches(2.2))
    for row in table.rows:
        for idx, cell in enumerate(row.cells):
            cell.width = widths[idx]

    add_caption(doc, "Table 1. Swing/AWT components and their locations.")

    add_heading_styled(doc, "3. Application Screenshots")
    add_paragraph(
        doc,
        "The figures below show the three primary tabs and the Add Student dialog, using the in-memory "
        "seed data (Alpha Team and two assigned tasks).",
        align="justify",
        space_after=4,
    )

    add_two_images(doc, SHOTS / "01-students-groups.png", SHOTS / "03-tasks.png", width=3.2)
    add_caption(doc, "Figure 2. Students & Groups tab (left) and Tasks & Assignment tab (right).")

    add_two_images(doc, SHOTS / "02-add-student-dialog.png", SHOTS / "04-group-summary-report.png", width=3.2)
    add_caption(doc, "Figure 3. Add Student dialog (left) and Group Summary Report (right).")

    add_two_images(doc, SHOTS / "05-member-contribution-report.png", SHOTS / "06-task-status-report.png", width=3.2)
    add_caption(
        doc,
        "Figure 4. Member Contribution Report (left) and Task Status Report (right).",
    )

    add_paragraph(
        doc,
        "Key features visible in these screens: adding and assigning students; the one-group membership "
        "rule (GO'AR Ritmunu Miracle remains Unassigned until placed in a group); creating tasks with a "
        "group, assignee, deadline, and status; and three reports. The group summary computes completion "
        "percentage, the member report computes a contribution score per student, and the task report lists "
        "every responsibility in a monospaced table.",
        align="justify",
        space_after=6,
    )

    add_heading_styled(doc, "4. Challenges and How They Were Overcome")
    challenges = [
        (
            "Applying MVC in Swing.",
            "Swing listeners naturally live on components, so a strict controller class for every click would have added ceremony without much benefit. We kept models free of UI code, used AppController only for thread-safe startup, and let each panel act as a local controller over the shared DataStore.",
        ),
        (
            "One student, one group.",
            "A naive add-to-group operation left students in multiple groups. Before assigning, the panel now removes the student from every existing group. Group.addMember() and removeMember() keep Student.groupId aligned with membership.",
        ),
        (
            "Keeping three tabs in sync.",
            "Because all screens share one DataStore, stale combo boxes were possible after an assignment. MainFrame registers a JTabbedPane change listener that refreshes the Students and Tasks panels whenever the user switches tabs.",
        ),
        (
            "Packed data-entry layouts.",
            "The task form needed labels and combo boxes aligned in two rows. GridBagLayout handled that form, while JSplitPane separated student and group management without forcing a fixed width.",
        ),
        (
            "Readable reports without a charting library.",
            "Progress and contribution values are calculated from streams over the responsibility list and printed in a non-editable, monospaced JTextArea. This met the reporting requirement without extra dependencies.",
        ),
    ]
    for title, body in challenges:
        para = doc.add_paragraph()
        para.paragraph_format.space_after = Pt(4)
        para.paragraph_format.space_before = Pt(1)
        para.paragraph_format.first_line_indent = Cm(0)
        run = para.add_run(title + " ")
        set_run_font(run, size=11, bold=True)
        run2 = para.add_run(body)
        set_run_font(run2, size=11)
        para.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY

    add_paragraph(
        doc,
        "Overall, the MVC split, a single DataStore, and tab-level refresh were enough to keep the interface "
        "consistent while remaining a compact academic Swing application.",
        align="justify",
        space_after=0,
        first_line=True,
    )

    doc.save(OUTPUT)
    print(f"Wrote {OUTPUT}")


if __name__ == "__main__":
    build()
