import cv2
import pytesseract
import re

# Path to the Tesseract executable
pytesseract.pytesseract.tesseract_cmd = r"C:\\Users\\ABISHA\\AppData\\Local\\Programs\\Tesseract-OCR\\tesseract.exe"

harcascade = "C:\\Users\\ABISHA\\PycharmProjects\\car_parking\\haarcascade_russian_plate_number.xml"

cap = cv2.VideoCapture("C:\\Users\\ABISHA\\PycharmProjects\\pythonProject\\edited.mp4")

window_width = 800
window_height = 600

# Create a named window with the specified dimensions
cv2.namedWindow("Result", cv2.WINDOW_NORMAL)
cv2.resizeWindow("Result", window_width, window_height)

min_area = 500
count = 0
detected_plates = []


def calculate_iou(box1, box2):
    x1, y1, w1, h1 = box1
    x2, y2, w2, h2 = box2

    # Calculate coordinates of intersection rectangle
    x_left = max(x1, x2)
    y_top = max(y1, y2)
    x_right = min(x1 + w1, x2 + w2)
    y_bottom = min(y1 + h1, y2 + h2)

    if x_right < x_left or y_bottom < y_top:
        return 0.0

    # Calculate area of intersection rectangle
    intersection_area = (x_right - x_left) * (y_bottom - y_top)

    # Calculate area of both bounding boxes
    area_box1 = w1 * h1
    area_box2 = w2 * h2

    # Calculate IoU
    iou = intersection_area / float(area_box1 + area_box2 - intersection_area)
    return iou



def preprocess_image(image):
    # Convert the ROI to grayscale
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    # Apply Gaussian blur to reduce noise
    blurred = cv2.GaussianBlur(gray, (5, 5), 0)
    return blurred


def is_valid_plate(text):
    # Define a regular expression pattern for a valid license plate format
    pattern = r'^[A-Z]\d{3}[A-Z]{2}$'
    #print("Expected pattern:", pattern)

    # Trim whitespace from the text
    text = text.strip()
    #print("Stripped text:", text)

    # Check if the extracted text matches the pattern (case-insensitive match)
    match = re.match(pattern, text, re.IGNORECASE)
    #print("Match object:", match)

    if match:
        return text
    else:
        #print("No match.")
        return False


while True:
    success, img = cap.read()
    if not success:
        print("Failed to read frame")
        break

    plate_cascade = cv2.CascadeClassifier(harcascade)
    img_gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    plates = plate_cascade.detectMultiScale(img_gray, 1.4, 3)

    selected_plates = []

    for (x, y, w, h) in plates:
        area = w * h

        if area > min_area:
            plate_box = (x, y, w, h)
            is_selected = True

            # Check IoU with previously selected plates
            for selected_plate_box in selected_plates:
                iou = calculate_iou(plate_box, selected_plate_box)
                if iou > 0.5:  # Adjust the IoU threshold as needed
                    is_selected = False
                    break

            if is_selected:
                selected_plates.append(plate_box)
                cv2.rectangle(img, (x, y), (x + w, y + h), (0, 255, 0), 2)
                #cv2.putText(img, "Number Plate", (x, y - 5), cv2.FONT_HERSHEY_COMPLEX_SMALL, 1, (255, 0, 255), 2)

                img_roi = img[y: y + h, x:x + w]

                # Preprocess the ROI
                img_roi_processed = preprocess_image(img_roi)

                # OCR
                plate_text = pytesseract.image_to_string(img_roi_processed, config='--psm 6')
                plate_text = ''.join(c for c in plate_text if c.isalnum())

                if is_valid_plate(plate_text):
                    print("Detected Plate Number:", plate_text)
                    cv2.putText(img, plate_text, (x, y - 5), cv2.FONT_HERSHEY_SIMPLEX, 2, (0, 0, 255), 3)
                    detected_plates.append(plate_text)
                cv2.imshow("ROI", img_roi)

    cv2.imshow("Result", img)

    if cv2.waitKey(1) & 0xFF == ord('s'):
        cv2.imwrite("plates/scanned_img_" + str(count) + ".jpg", img_roi)
        cv2.rectangle(img, (0, 200), (640, 300), (0, 255, 0), cv2.FILLED)
        cv2.putText(img, "Plate Saved", (150, 265), cv2.FONT_HERSHEY_COMPLEX_SMALL, 2, (0, 0, 255), 2)
        cv2.imshow("Results", img)
        cv2.waitKey(500)
        count += 1

cap.release()
cv2.destroyAllWindows()