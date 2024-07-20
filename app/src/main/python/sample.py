import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart

def send_email(sender_email, receiver_email, subject, message, smtp_server, smtp_port, smtp_username, smtp_password):
    # Create message container
    msg = MIMEMultipart()
    msg['From'] = sender_email
    msg['To'] = receiver_email
    msg['Subject'] = subject

    # Attach message to email
    msg.attach(MIMEText(message, 'plain'))

    # Connect to SMTP server
    server = smtplib.SMTP(smtp_server, smtp_port)
    server.starttls()

    # Login to SMTP server
    server.login(smtp_username, smtp_password)

    # Send email
    server.sendmail(sender_email, receiver_email, msg.as_string())

    # Close connection
    server.quit()

# Example usage:
sender_email = 'abi2327sha@gmail.com'
receiver_email = 'abi2327sha@gmail.com'
subject = 'ParkSavvy'
message = ('Date = 9-April'
           'Duration = 2 hours'
           'Vehicle = Benz'
           '')
smtp_server = 'smtp.gmail.com'
smtp_port = 587
smtp_username = 'abi.2711.sha@gmail.com'
smtp_password = 'ukir nree zxns saqk'

send_email(sender_email, receiver_email, subject, message, smtp_server, smtp_port, smtp_username, smtp_password)