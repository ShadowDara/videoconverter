package de.shadowdara.videoconverter

import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.filechooser.FileSystemView

fun createUI(profileNames: Array<String>, config: Config) {
    val frame = JFrame("Video Converter")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.layout = BorderLayout()

    val mainPanel = JPanel()
    mainPanel.layout = BoxLayout(mainPanel, BoxLayout.Y_AXIS)
    mainPanel.border = BorderFactory.createEmptyBorder(20, 20, 20, 20)

    val title = JLabel("Video Converter")
    title.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20))
    title.alignmentX = Component.LEFT_ALIGNMENT

    // Variablen, um Auswahl zu speichern
    var selectedFiles: List<String> = emptyList()
    var selectedExportFolder: String = ""

    // Choose Files Button
    val chooseFileButton = JButton("Choose Files")
    chooseFileButton.alignmentX = Component.LEFT_ALIGNMENT

    val chosenFilesLabel = JLabel("No files chosen")
    chosenFilesLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0))
    chosenFilesLabel.alignmentX = Component.LEFT_ALIGNMENT

    chooseFileButton.addActionListener {
        val chooser = JFileChooser()
        chooser.fileSelectionMode = JFileChooser.FILES_ONLY
        chooser.isMultiSelectionEnabled = true
        val filter = FileNameExtensionFilter("Video files (MP4)", "mp4")
        chooser.fileFilter = filter

        val result = chooser.showOpenDialog(frame)

        if (result == JFileChooser.APPROVE_OPTION) {
            val files = chooser.selectedFiles
            if (files.isNotEmpty()) {
                selectedFiles = files.map { it.absolutePath }
                val filePathsHtml = selectedFiles.joinToString("<br>")
                chosenFilesLabel.text = "<html>$filePathsHtml</html>"
            } else {
                chosenFilesLabel.text = "No files chosen"
                selectedFiles = emptyList()
            }
        } else {
            chosenFilesLabel.text = "No files chosen"
            selectedFiles = emptyList()
        }
    }

    // Choose Export Folder Button
    val chooseExportFolderButton = JButton("Choose Export Folder")
    chooseExportFolderButton.alignmentX = Component.LEFT_ALIGNMENT

    val chosenFolderLabel = JLabel("No Export Folder Chosen")
    chosenFolderLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0))
    chosenFolderLabel.alignmentX = Component.LEFT_ALIGNMENT

    chooseExportFolderButton.addActionListener {
        val chooser = JFileChooser(FileSystemView.getFileSystemView().homeDirectory)
        chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY

        val result = chooser.showOpenDialog(frame)

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedExportFolder = chooser.selectedFile.absolutePath
            chosenFolderLabel.text = selectedExportFolder
        } else {
            println("Selection cancelled.")
        }
    }

    // Profile Dropdown
    val label = JLabel("Choose a Profile:")
    label.alignmentX = Component.LEFT_ALIGNMENT

    val dropdown = JComboBox(profileNames)
    dropdown.maximumSize = Dimension(Int.MAX_VALUE, dropdown.preferredSize.height)
    dropdown.alignmentX = Component.LEFT_ALIGNMENT

    // Apply Button
    val applyButton = JButton("Use Profile")
    applyButton.alignmentX = Component.LEFT_ALIGNMENT

    applyButton.addActionListener {
        val selectedProfile = dropdown.selectedItem as? String
        if (selectedProfile == null) {
            JOptionPane.showMessageDialog(frame, "No profile selected")
            return@addActionListener
        }

        if (selectedFiles.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No files selected")
            return@addActionListener
        }

        if (selectedExportFolder.isBlank()) {
            JOptionPane.showMessageDialog(frame, "No export folder selected")
            return@addActionListener
        }

        // Hier kannst du deine convert-Funktion aufrufen
        convert(selectedProfile, selectedExportFolder, selectedFiles, config)

        JOptionPane.showMessageDialog(frame, "Conversion Finished!")
    }

    // UI zusammenbauen
    mainPanel.add(title)
    mainPanel.add(chooseFileButton)
    mainPanel.add(chosenFilesLabel)
    mainPanel.add(chooseExportFolderButton)
    mainPanel.add(chosenFolderLabel)
    mainPanel.add(label)
    mainPanel.add(Box.createVerticalStrut(10))
    mainPanel.add(dropdown)
    mainPanel.add(Box.createVerticalStrut(20))
    mainPanel.add(applyButton)

    frame.contentPane.add(mainPanel, BorderLayout.CENTER)
    frame.pack()
    frame.setLocationRelativeTo(null)
    frame.isVisible = true
}

