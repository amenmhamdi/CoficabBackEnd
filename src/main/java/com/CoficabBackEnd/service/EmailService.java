package com.CoficabBackEnd.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendEmail(String to, String subject, String content) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

		try {
			helper.setFrom("noreply@example.com");
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true); // Set the second parameter to true for HTML content
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			// Handle the exception as needed
			e.printStackTrace();
		}
	}

	public void sendPasswordResetEmail(String to, String resetLink) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");

			ClassPathResource image1Resource = new ClassPathResource("static/assets/img/image-1.png");
			ClassPathResource image2Resource = new ClassPathResource("static/assets/img/image-2.png");

			// Add images as inline attachments
			helper.addInline("image1", image1Resource);
			helper.addInline("image2", image2Resource);

			String emailContent = "<!DOCTYPE HTML\r\n"
					+ "  PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n"
					+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\"\r\n"
					+ "  xmlns:o=\"urn:schemas-microsoft-com:office:office\">\r\n"
					+ "\r\n"
					+ "<head>\r\n"
					+ "  <!--[if gte mso 9]>\r\n"
					+ "<xml>\r\n"
					+ "  <o:OfficeDocumentSettings>\r\n"
					+ "    <o:AllowPNG/>\r\n"
					+ "    <o:PixelsPerInch>96</o:PixelsPerInch>\r\n"
					+ "  </o:OfficeDocumentSettings>\r\n"
					+ "</xml>\r\n"
					+ "<![endif]-->\r\n"
					+ "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n"
					+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
					+ "  <meta name=\"x-apple-disable-message-reformatting\">\r\n"
					+ "  <!--[if !mso]><!-->\r\n"
					+ "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><!--<![endif]-->\r\n"
					+ "  <title></title>\r\n"
					+ "\r\n"
					+ "  <style type=\"text/css\">\r\n"
					+ "    @media only screen and (min-width: 620px) {\r\n"
					+ "      .u-row {\r\n"
					+ "        width: 600px !important;\r\n"
					+ "      }\r\n"
					+ "\r\n"
					+ "      .u-row .u-col {\r\n"
					+ "        vertical-align: top;\r\n"
					+ "      }\r\n"
					+ "\r\n"
					+ "      .u-row .u-col-100 {\r\n"
					+ "        width: 600px !important;\r\n"
					+ "      }\r\n"
					+ "\r\n"
					+ "    }\r\n"
					+ "\r\n"
					+ "    @media (max-width: 620px) {\r\n"
					+ "      .u-row-container {\r\n"
					+ "        max-width: 100% !important;\r\n"
					+ "        padding-left: 0px !important;\r\n"
					+ "        padding-right: 0px !important;\r\n"
					+ "      }\r\n"
					+ "\r\n"
					+ "      .u-row .u-col {\r\n"
					+ "        min-width: 320px !important;\r\n"
					+ "        max-width: 100% !important;\r\n"
					+ "        display: block !important;\r\n"
					+ "      }\r\n"
					+ "\r\n"
					+ "      .u-row {\r\n"
					+ "        width: 100% !important;\r\n"
					+ "      }\r\n"
					+ "\r\n"
					+ "      .u-col {\r\n"
					+ "        width: 100% !important;\r\n"
					+ "      }\r\n"
					+ "\r\n"
					+ "      .u-col>div {\r\n"
					+ "        margin: 0 auto;\r\n"
					+ "      }\r\n"
					+ "    }\r\n"
					+ "\r\n"
					+ "    body {\r\n"
					+ "      margin: 0;\r\n"
					+ "      padding: 0;\r\n"
					+ "    }\r\n"
					+ "\r\n"
					+ "    table,\r\n"
					+ "    tr,\r\n"
					+ "    td {\r\n"
					+ "      vertical-align: top;\r\n"
					+ "      border-collapse: collapse;\r\n"
					+ "    }\r\n"
					+ "\r\n"
					+ "    p {\r\n"
					+ "      margin: 0;\r\n"
					+ "    }\r\n"
					+ "\r\n"
					+ "    .ie-container table,\r\n"
					+ "    .mso-container table {\r\n"
					+ "      table-layout: fixed;\r\n"
					+ "    }\r\n"
					+ "\r\n"
					+ "    * {\r\n"
					+ "      line-height: inherit;\r\n"
					+ "    }\r\n"
					+ "\r\n"
					+ "    a[x-apple-data-detectors='true'] {\r\n"
					+ "      color: inherit !important;\r\n"
					+ "      text-decoration: none !important;\r\n"
					+ "    }\r\n"
					+ "\r\n"
					+ "    table,\r\n"
					+ "    td {\r\n"
					+ "      color: #000000;\r\n"
					+ "    }\r\n"
					+ "\r\n"
					+ "    #u_body a {\r\n"
					+ "      color: #161a39;\r\n"
					+ "      text-decoration: underline;\r\n"
					+ "    }\r\n"
					+ "  </style>\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "  <!--[if !mso]><!-->\r\n"
					+ "  <link href=\"https://fonts.googleapis.com/css?family=Lato:400,700&display=swap\" rel=\"stylesheet\" type=\"text/css\">\r\n"
					+ "  <link href=\"https://fonts.googleapis.com/css?family=Lato:400,700&display=swap\" rel=\"stylesheet\" type=\"text/css\">\r\n"
					+ "  <!--<![endif]-->\r\n"
					+ "\r\n"
					+ "</head>\r\n"
					+ "\r\n"
					+ "<body class=\"clean-body u_body\"\r\n"
					+ "  style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #f9f9f9;color: #000000\">\r\n"
					+ "  <!--[if IE]><div class=\"ie-container\"><![endif]-->\r\n"
					+ "  <!--[if mso]><div class=\"mso-container\"><![endif]-->\r\n"
					+ "  <table id=\"u_body\"\r\n"
					+ "    style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #f9f9f9;width:100%\"\r\n"
					+ "    cellpadding=\"0\" cellspacing=\"0\">\r\n"
					+ "    <tbody>\r\n"
					+ "      <tr style=\"vertical-align: top\">\r\n"
					+ "        <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">\r\n"
					+ "          <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color: #f9f9f9;\"><![endif]-->\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "          <div class=\"u-row-container\" style=\"padding: 0px;background-color: #f9f9f9\">\r\n"
					+ "            <div class=\"u-row\"\r\n"
					+ "              style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #f9f9f9;\">\r\n"
					+ "              <div\r\n"
					+ "                style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\r\n"
					+ "                <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: #f9f9f9;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #f9f9f9;\"><![endif]-->\r\n"
					+ "\r\n"
					+ "                <!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\r\n"
					+ "                <div class=\"u-col u-col-100\"\r\n"
					+ "                  style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n"
					+ "                  <div style=\"height: 100%;width: 100% !important;\">\r\n"
					+ "                    <!--[if (!mso)&(!IE)]><!-->\r\n"
					+ "                    <div\r\n"
					+ "                      style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\r\n"
					+ "                      <!--<![endif]-->\r\n"
					+ "\r\n"
					+ "                      <table style=\"font-family:'Lato',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\"\r\n"
					+ "                        width=\"100%\" border=\"0\">\r\n"
					+ "                        <tbody>\r\n"
					+ "                          <tr>\r\n"
					+ "                            <td\r\n"
					+ "                              style=\"overflow-wrap:break-word;word-break:break-word;padding:15px;font-family:'Lato',sans-serif;\"\r\n"
					+ "                              align=\"left\">\r\n"
					+ "\r\n"
					+ "                              <table height=\"0px\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\r\n"
					+ "                                style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 1px solid #f9f9f9;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\r\n"
					+ "                                <tbody>\r\n"
					+ "                                  <tr style=\"vertical-align: top\">\r\n"
					+ "                                    <td\r\n"
					+ "                                      style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\r\n"
					+ "                                      <span>&#160;</span>\r\n"
					+ "                                    </td>\r\n"
					+ "                                  </tr>\r\n"
					+ "                                </tbody>\r\n"
					+ "                              </table>\r\n"
					+ "\r\n"
					+ "                            </td>\r\n"
					+ "                          </tr>\r\n"
					+ "                        </tbody>\r\n"
					+ "                      </table>\r\n"
					+ "\r\n"
					+ "                      <!--[if (!mso)&(!IE)]><!-->\r\n"
					+ "                    </div><!--<![endif]-->\r\n"
					+ "                  </div>\r\n"
					+ "                </div>\r\n"
					+ "                <!--[if (mso)|(IE)]></td><![endif]-->\r\n"
					+ "                <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n"
					+ "              </div>\r\n"
					+ "            </div>\r\n"
					+ "          </div>\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "          <div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\r\n"
					+ "            <div class=\"u-row\"\r\n"
					+ "              style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #161a39;\">\r\n"
					+ "              <div\r\n"
					+ "                style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\r\n"
					+ "                <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #161a39;\"><![endif]-->\r\n"
					+ "\r\n"
					+ "                <!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\r\n"
					+ "                <div class=\"u-col u-col-100\"\r\n"
					+ "                  style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n"
					+ "                  <div style=\"height: 100%;width: 100% !important;\">\r\n"
					+ "                    <!--[if (!mso)&(!IE)]><!-->\r\n"
					+ "                    <div\r\n"
					+ "                      style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\r\n"
					+ "                      <!--<![endif]-->\r\n"
					+ "\r\n"
					+ "                      <table style=\"font-family:'Lato',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\"\r\n"
					+ "                        width=\"100%\" border=\"0\">\r\n"
					+ "                        <tbody>\r\n"
					+ "                          <tr>\r\n"
					+ "                            <td\r\n"
					+ "                              style=\"overflow-wrap:break-word;word-break:break-word;padding:35px;font-family:'Lato',sans-serif;\"\r\n"
					+ "                              align=\"left\">\r\n"
					+ "\r\n"
					+ "                              <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\r\n"
					+ "                                <tr>\r\n"
					+ "                            <td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">\r\n"
					+ "\r\n"
					+ "  <img align=\"center\" border=\"0\" src=\"https://cdn.templates.unlayer.com/assets/1593141680866-reset.png\" alt=\"Image\" title=\"Image\" \r\n"
					+ "   style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: inline-block !important;border: none;height: auto;float: none;width: 16%;max-width: 92.8px;\" width=\"92.8\"/>\r\n"
					+ "\r\n"
					+ "</td>\r\n"
					+ "                                </tr>\r\n"
					+ "                              </table>\r\n"
					+ "\r\n"
					+ "                            </td>\r\n"
					+ "                          </tr>\r\n"
					+ "                        </tbody>\r\n"
					+ "                      </table>\r\n"
					+ "\r\n"
					+ "                      <table style=\"font-family:'Lato',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\"\r\n"
					+ "                        width=\"100%\" border=\"0\">\r\n"
					+ "                        <tbody>\r\n"
					+ "                          <tr>\r\n"
					+ "                            <td\r\n"
					+ "                              style=\"overflow-wrap:break-word;word-break:break-word;padding:0px 10px 30px;font-family:'Lato',sans-serif;\"\r\n"
					+ "                              align=\"left\">\r\n"
					+ "\r\n"
					+ "                              <div style=\"font-size: 14px; line-height: 140%; text-align: left; word-wrap: break-word;\">\r\n"
					+ "                                <p style=\"font-size: 14px; line-height: 140%; text-align: center;\"><span\r\n"
					+ "                                    style=\"font-size: 28px; line-height: 39.2px; color: #ffffff; font-family: Lato, sans-serif;\">Please\r\n"
					+ "                                    reset your password </span></p>\r\n"
					+ "                              </div>\r\n"
					+ "\r\n"
					+ "                            </td>\r\n"
					+ "                          </tr>\r\n"
					+ "                        </tbody>\r\n"
					+ "                      </table>\r\n"
					+ "\r\n"
					+ "                      <!--[if (!mso)&(!IE)]><!-->\r\n"
					+ "                    </div><!--<![endif]-->\r\n"
					+ "                  </div>\r\n"
					+ "                </div>\r\n"
					+ "                <!--[if (mso)|(IE)]></td><![endif]-->\r\n"
					+ "                <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n"
					+ "              </div>\r\n"
					+ "            </div>\r\n"
					+ "          </div>\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "          <div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\r\n"
					+ "            <div class=\"u-row\"\r\n"
					+ "              style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #ffffff;\">\r\n"
					+ "              <div\r\n"
					+ "                style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\r\n"
					+ "                <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #ffffff;\"><![endif]-->\r\n"
					+ "\r\n"
					+ "                <!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\r\n"
					+ "                <div class=\"u-col u-col-100\"\r\n"
					+ "                  style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n"
					+ "                  <div style=\"height: 100%;width: 100% !important;\">\r\n"
					+ "                    <!--[if (!mso)&(!IE)]><!-->\r\n"
					+ "                    <div\r\n"
					+ "                      style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\r\n"
					+ "                      <!--<![endif]-->\r\n"
					+ "\r\n"
					+ "                      <table style=\"font-family:'Lato',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\"\r\n"
					+ "                        width=\"100%\" border=\"0\">\r\n"
					+ "                        <tbody>\r\n"
					+ "                          <tr>\r\n"
					+ "                            <td\r\n"
					+ "                              style=\"overflow-wrap:break-word;word-break:break-word;padding:40px 40px 30px;font-family:'Lato',sans-serif;\"\r\n"
					+ "                              align=\"left\">\r\n"
					+ "\r\n"
					+ "                              <div style=\"font-size: 14px; line-height: 140%; text-align: left; word-wrap: break-word;\">\r\n"
					+ "                                <p style=\"font-size: 14px; line-height: 140%;\"><span\r\n"
					+ "                                    style=\"font-size: 18px; line-height: 25.2px; color: #666666;\">Hello,</span></p>\r\n"
					+ "                                <p style=\"font-size: 14px; line-height: 140%;\">&nbsp;</p>\r\n"
					+ "                                <p style=\"font-size: 14px; line-height: 140%;\"><span\r\n"
					+ "                                    style=\"font-size: 18px; line-height: 25.2px; color: #666666;\">We have sent you this\r\n"
					+ "                                    email in response to your request to reset your password.</span></p>\r\n"
					+ "                                <p style=\"font-size: 14px; line-height: 140%;\">&nbsp;</p>\r\n"
					+ "                                <p style=\"font-size: 14px; line-height: 140%;\"><span\r\n"
					+ "                                    style=\"font-size: 18px; line-height: 25.2px; color: #666666;\">To reset your\r\n"
					+ "                                    password, please follow the link below: </span></p>\r\n"
					+ "                              </div>\r\n"
					+ "\r\n"
					+ "                            </td>\r\n"
					+ "                          </tr>\r\n"
					+ "                        </tbody>\r\n"
					+ "                      </table>\r\n"
					+ "\r\n"
					+ "                      <table style=\"font-family:'Lato',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\"\r\n"
					+ "                        width=\"100%\" border=\"0\">\r\n"
					+ "                        <tbody>\r\n"
					+ "                          <tr>\r\n"
					+ "                            <td\r\n"
					+ "                              style=\"overflow-wrap:break-word;word-break:break-word;padding:0px 40px;font-family:'Lato',sans-serif;\"\r\n"
					+ "                              align=\"left\">\r\n"
					+ "\r\n"
					+ "                              <!--[if mso]><style>.v-button {background: transparent !important;}</style><![endif]-->\r\n"
					+ "                              <div align=\"left\">\r\n"
					+ "                                <a href=\"" + resetLink
					+ "\" target=\"_blank\" class=\"v-button\"\r\n"
					+ "                                   style=\"box-sizing: border-box;display: inline-block;text-decoration: none;-webkit-text-size-adjust: none;text-align: center;color: #FFFFFF; background-color: #18163a; border-radius: 1px;-webkit-border-radius: 1px; -moz-border-radius: 1px; width:auto; max-width:100%; overflow-wrap: break-word; word-break: break-word; word-wrap:break-word; mso-border-alt: none;font-size: 14px;\">\r\n"
					+ "                                    <span style=\"display:block;padding:15px 40px;line-height:120%;\">\r\n"
					+ "                                        <span style=\"font-size: 18px; line-height: 21.6px;\">Reset Password</span>\r\n"
					+ "                                    </span>\r\n"
					+ "                                </a>\r\n"
					+ "                            </div>\r\n"
					+ "\r\n"
					+ "                            </td>\r\n"
					+ "                          </tr>\r\n"
					+ "                        </tbody>\r\n"
					+ "                      </table>\r\n"
					+ "\r\n"
					+ "                      <table style=\"font-family:'Lato',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\"\r\n"
					+ "                        width=\"100%\" border=\"0\">\r\n"
					+ "                        <tbody>\r\n"
					+ "                          <tr>\r\n"
					+ "                            <td\r\n"
					+ "                              style=\"overflow-wrap:break-word;word-break:break-word;padding:40px 40px 30px;font-family:'Lato',sans-serif;\"\r\n"
					+ "                              align=\"left\">\r\n"
					+ "\r\n"
					+ "                              <div style=\"font-size: 14px; line-height: 140%; text-align: left; word-wrap: break-word;\">\r\n"
					+ "                                <p style=\"font-size: 14px; line-height: 140%;\"><span\r\n"
					+ "                                    style=\"color: #888888; font-size: 14px; line-height: 19.6px;\"><em><span\r\n"
					+ "                                        style=\"font-size: 16px; line-height: 22.4px;\">Please ignore this email if you\r\n"
					+ "                                        did not request a password change.</span></em></span><br /><span\r\n"
					+ "                                    style=\"color: #888888; font-size: 14px; line-height: 19.6px;\"><em><span\r\n"
					+ "                                        style=\"font-size: 16px; line-height: 22.4px;\">&nbsp;</span></em></span></p>\r\n"
					+ "                              </div>\r\n"
					+ "\r\n"
					+ "                            </td>\r\n"
					+ "                          </tr>\r\n"
					+ "                        </tbody>\r\n"
					+ "                      </table>\r\n"
					+ "\r\n"
					+ "                      <!--[if (!mso)&(!IE)]><!-->\r\n"
					+ "                    </div><!--<![endif]-->\r\n"
					+ "                  </div>\r\n"
					+ "                </div>\r\n"
					+ "                <!--[if (mso)|(IE)]></td><![endif]-->\r\n"
					+ "                <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n"
					+ "              </div>\r\n"
					+ "            </div>\r\n"
					+ "          </div>\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "          <div class=\"u-row-container\" style=\"padding: 0px;background-color: #f9f9f9\">\r\n"
					+ "            <div class=\"u-row\"\r\n"
					+ "              style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #1c103b;\">\r\n"
					+ "              <div\r\n"
					+ "                style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\r\n"
					+ "                <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: #f9f9f9;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #1c103b;\"><![endif]-->\r\n"
					+ "\r\n"
					+ "                <!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\r\n"
					+ "                <div class=\"u-col u-col-100\"\r\n"
					+ "                  style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n"
					+ "                  <div style=\"height: 100%;width: 100% !important;\">\r\n"
					+ "                    <!--[if (!mso)&(!IE)]><!-->\r\n"
					+ "                    <div\r\n"
					+ "                      style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\r\n"
					+ "                      <!--<![endif]-->\r\n"
					+ "\r\n"
					+ "                      <table style=\"font-family:'Lato',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\"\r\n"
					+ "                        width=\"100%\" border=\"0\">\r\n"
					+ "                        <tbody>\r\n"
					+ "                          <tr>\r\n"
					+ "                            <td\r\n"
					+ "                              style=\"overflow-wrap:break-word;word-break:break-word;padding:15px;font-family:'Lato',sans-serif;\"\r\n"
					+ "                              align=\"left\">\r\n"
					+ "\r\n"
					+ "                              <table height=\"0px\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\r\n"
					+ "                                style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 1px solid #1c103b;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\r\n"
					+ "                                <tbody>\r\n"
					+ "                                  <tr style=\"vertical-align: top\">\r\n"
					+ "                                    <td\r\n"
					+ "                                      style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\r\n"
					+ "                                      <span>&#160;</span>\r\n"
					+ "                                    </td>\r\n"
					+ "                                  </tr>\r\n"
					+ "                                </tbody>\r\n"
					+ "                              </table>\r\n"
					+ "\r\n"
					+ "                            </td>\r\n"
					+ "                          </tr>\r\n"
					+ "                        </tbody>\r\n"
					+ "                      </table>\r\n"
					+ "\r\n"
					+ "                      <!--[if (!mso)&(!IE)]><!-->\r\n"
					+ "                    </div><!--<![endif]-->\r\n"
					+ "                  </div>\r\n"
					+ "                </div>\r\n"
					+ "                <!--[if (mso)|(IE)]></td><![endif]-->\r\n"
					+ "                <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n"
					+ "              </div>\r\n"
					+ "            </div>\r\n"
					+ "          </div>\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "          <div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\r\n"
					+ "            <div class=\"u-row\"\r\n"
					+ "              style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #f9f9f9;\">\r\n"
					+ "              <div\r\n"
					+ "                style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\r\n"
					+ "                <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #f9f9f9;\"><![endif]-->\r\n"
					+ "\r\n"
					+ "                <!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\r\n"
					+ "                <div class=\"u-col u-col-100\"\r\n"
					+ "                  style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n"
					+ "                  <div style=\"height: 100%;width: 100% !important;\">\r\n"
					+ "                    <!--[if (!mso)&(!IE)]><!-->\r\n"
					+ "                    <div\r\n"
					+ "                      style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\">\r\n"
					+ "                      <!--<![endif]-->\r\n"
					+ "\r\n"
					+ "                      <table style=\"font-family:'Lato',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\"\r\n"
					+ "                        width=\"100%\" border=\"0\">\r\n"
					+ "                        <tbody>\r\n"
					+ "                          <tr>\r\n"
					+ "                            <td\r\n"
					+ "                              style=\"overflow-wrap:break-word;word-break:break-word;padding:0px 40px 30px 20px;font-family:'Lato',sans-serif;\"\r\n"
					+ "                              align=\"left\">\r\n"
					+ "\r\n"
					+ "                              <div style=\"font-size: 14px; line-height: 140%; text-align: left; word-wrap: break-word;\">\r\n"
					+ "\r\n"
					+ "                              </div>\r\n"
					+ "\r\n"
					+ "                            </td>\r\n"
					+ "                          </tr>\r\n"
					+ "                        </tbody>\r\n"
					+ "                      </table>\r\n"
					+ "\r\n"
					+ "                      <!--[if (!mso)&(!IE)]><!-->\r\n"
					+ "                    </div><!--<![endif]-->\r\n"
					+ "                  </div>\r\n"
					+ "                </div>\r\n"
					+ "                <!--[if (mso)|(IE)]></td><![endif]-->\r\n"
					+ "                <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n"
					+ "              </div>\r\n"
					+ "            </div>\r\n"
					+ "          </div>\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "          <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\r\n"
					+ "        </td>\r\n"
					+ "      </tr>\r\n"
					+ "    </tbody>\r\n"
					+ "  </table>\r\n"
					+ "  <!--[if mso]></div><![endif]-->\r\n"
					+ "  <!--[if IE]></div><![endif]-->\r\n"
					+ "</body>\r\n"
					+ "\r\n"
					+ "</html>";
			sendEmail(to, "Password Reset", emailContent);
		} catch (MessagingException e) {
			// Handle the exception as needed
			e.printStackTrace();
		}
	}
}
