package com.sape.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class EmailManager {
    private static List<String> fileList = new ArrayList<String>();
    private static final Logger LOG = Logger.getLogger(EmailManager.class);
    static String fs = Constants.FS;

    private String emailTo, emailFrom, emailHost, emailSubject, emailAttachReport, reportsPath;

    public EmailManager(String emailHost, String emailTo, String emailFrom, String emailSubject, String emailAttachReport,
            String reportsPath) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        this.emailHost = emailHost;
        this.emailFrom = emailFrom;
        this.emailTo = emailTo;
        this.emailAttachReport = emailAttachReport;
        this.emailSubject = emailSubject;
        this.reportsPath = reportsPath;
    }

    public static void main(String[] args) {
        EmailManager email = new EmailManager("HKGoutlook.lb.macbank", "Abhishek.Pandey@maquarie.com",
                "Abhishek.Pandey@maquarie.com", "automation", "", "");
        email.sendReport();

    }

    public void sendReport() {
        Message message = createReportMessage();
        try {
            Transport.send(message);
        } catch (MessagingException me) {
            LOG.error("sendReport ", me);
        }
    }

    private Message createReportMessage() {
        Message msg = new MimeMessage(getSession());
        InternetAddress sender;
        try {
            sender = new InternetAddress(emailFrom);
            msg.setFrom(sender);
            msg.setRecipients(Message.RecipientType.TO, getRecipients());
            msg.setSubject(getEmailSubject());
            msg.setContent(getEmailContent());
        } catch (MessagingException e) {
            LOG.error("createReportMessage", e);
        }
        return msg;
    }

    private Session getSession() {
        Properties props = getProperties();
        Session session = Session.getInstance(props);

        session.setDebug(true);
        return session;
    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", emailHost);
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.smtp.auth", "false");
        return props;
    }

    private InternetAddress[] getRecipients() throws AddressException {
        List<InternetAddress> recipients = new ArrayList<InternetAddress>();
        String mailList = emailTo;
        try {
            for (String email : mailList.split(",")) {
                recipients.add(new InternetAddress(email));
            }
        } catch (AddressException e) {
            LOG.error("createReportMessage", e);
        }
        return recipients.toArray(new InternetAddress[recipients.size()]);
    }

    private String getEmailSubject() {
        return emailSubject;
    }

    private Multipart getEmailContent() {
        MimeMultipart multiPart = new MimeMultipart();
        try {
            BodyPart messagePart = getEmailBody();
            multiPart.addBodyPart(messagePart);

            if ("Y".equals(emailAttachReport)) {
                BodyPart attachmentPart = getEmailAttachment();
                multiPart.addBodyPart(attachmentPart);
            }

        } catch (MessagingException e) {
            LOG.error("getEmailContent", e);
        }

        return multiPart;
    }

    private BodyPart getEmailBody() {
        BodyPart messagePart = new MimeBodyPart();
        try {
            messagePart.setContent(createEmail(), "text/html");
        } catch (MessagingException e) {
            LOG.error("getEmailBody", e);
        }
        return messagePart;
    }

    public String createEmail() {
        StringBuilder result = new StringBuilder();

        result.append(createHeader());
        result.append(createSummary());
        result.append(createReportLink());

        return result.toString();
    }

    private static String createHeader() {
        StringBuilder result = new StringBuilder();

        result.append("<html><body>");
        result.append("<h3>Test Execution Summary</h3>");

        return result.toString();
    }

    private String createSummary() {
        StringBuilder result = new StringBuilder();

        result.append("<p><table border=0 cellspacing=1 cellpadding=0 align=left>");
        result.append("<tr><td width=200><b>Test Name</b></td>");
        result.append("<td>");

        result.append("</td></tr>");
        result.append("<tr><td width=200><b>Execution Date</b></td>");
        result.append("<td>");

        result.append("</td></tr>");
        result.append("<tr><td width=200><b>Status</b></td>");
        result.append("<td><font color=");

        result.append("><b>");

        result.append("</b></font></td></tr>");
        result.append("<tr><td width=200><b>Total Passed</b></td>");
        result.append("<td>");

        result.append("</td></tr>");
        result.append("<tr><td width=200><b>Total Failed</b></td>");
        result.append("<td>");

        result.append("</td></tr>");
        result.append("<tr><td width=200><b>Execution Duration</b></td>");
        result.append("<td>");

        result.append("</td></tr>");

        return result.toString();
    }

    private static String createReportLink() {
        StringBuilder result = new StringBuilder();

        result.append("<tr><td width=200><a href='");
        result.append("report url");
        result.append("'><b>Detailed report</b></td></tr>");
        result.append("</table>");
        result.append("</body></html>");

        return result.toString();
    }

    private BodyPart getEmailAttachment() {
        MimeBodyPart attachmentPart = new MimeBodyPart();
        try {
            attachmentPart.attachFile(getAttachmentFile());
        } catch (IOException | MessagingException e) {
            LOG.error("failed to get email attachment", e);
        }
        return attachmentPart;
    }

    private File getAttachmentFile() {
        File reportDir = new File(reportsPath);

        return createArchive(reportDir);
    }

    public File createArchive(File archiveSource) {
        String sourceDir = archiveSource.getAbsolutePath();
        String archiveName = archiveSource.getName();
        String archiveFilePath = archiveSource.getParentFile().getAbsolutePath() + EmailManager.fs + archiveName + ".zip";

        generateFileList(sourceDir, archiveSource);
        createArchive(sourceDir, archiveFilePath);
        return new File(archiveFilePath);
    }

    /**
     * Zip it
     * 
     * @param zipFile
     *            output ZIP file location
     */
    private void createArchive(String sourceDir, String zipFile) {
        byte[] buffer = new byte[1024];

        try {
            FileOutputStream fileOS = new FileOutputStream(zipFile);
            ZipOutputStream zipOS = new ZipOutputStream(fileOS);

            for (String file : fileList) {
                ZipEntry zipEntry = new ZipEntry(file);
                zipOS.putNextEntry(zipEntry);

                FileInputStream in = new FileInputStream(sourceDir + EmailManager.fs + file);

                int len;
                while ((len = in.read(buffer)) > 0) {
                    zipOS.write(buffer, 0, len);
                }
                in.close();
            }

            zipOS.closeEntry();
            zipOS.close();

        } catch (IOException e) {
            LOG.error("createArchive", e);
        }
    }

    /**
     * Traverse a directory and get all files, and add the file into fileList
     * 
     * @param node
     *            file or directory
     */
    private static void generateFileList(String sourceDir, File node) {

        // add file only
        if (node.isFile()) {
            fileList.add(generateZipEntry(sourceDir, node.getAbsolutePath()));
        }

        if (node.isDirectory()) {
            String[] subNode = node.list();
            for (String filename : subNode) {
                generateFileList(sourceDir, new File(node, filename));
            }
        }

    }

    /**
     * Format the file path for zip
     * 
     * @param file
     *            file path
     * @return Formatted file path
     */
    private static String generateZipEntry(String sourceDir, String file) {
        return file.substring(sourceDir.length() + 1, file.length());
    }

}
