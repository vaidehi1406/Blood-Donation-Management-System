# Blood-Donation-Management-System
 Java Swing application is a Blood Donation Management System with a graphical user interface for adding, deleting, searching, updating, and displaying blood donors, utilizing a MySQL database for storage

---

This Java code represents a simple Blood Donation Management System using a graphical user interface (GUI) built with Java Swing. The system allows users to perform basic operations such as adding donors, deleting donors, searching for donors based on blood group, updating donor information, and displaying the list of all donors. The data is stored in a MySQL database.

---

Here's a breakdown of the major components and functionalities:

**GUI Components:**

* Text fields for entering donor information (name, blood group, address, phone number).
* Buttons for adding, deleting, searching, updating, and displaying donors.
* A text area for displaying donor information.

**Database Connection:**

* The system connects to a MySQL database named "blood_donation_management" on the local machine using the root user and a password.

**Database Operations:**

* Prepared statements are used for database operations to prevent SQL injection.
* Four operations are supported: Insert (add donor), Delete (delete donor by blood group), Select (search for donors by blood group), and Update (update donor information by blood group).

**Load and Display Donors:**

* The system loads existing donors from the database on startup and displays them in the text area.
* There is a method (loadDonors()) that retrieves all donors from the database and updates the UI.

**Action Listeners:**

* Action listeners are implemented for each button to handle user interactions.
* For example, the "Add Donor" button triggers the insertion of a new donor into the database and updates the UI with the new donor's information.

**Closing Connection:**

* The database connection and prepared statements are closed when the application window is closed.

**Main Method:**

* The main method initializes the GUI and runs the application on the Event Dispatch Thread using SwingUtilities.invokeLater().

**Additional Functionality:**

* The code includes a "Display Donors" button to show the list of all donors.
* There are methods (clearInputFields() and reloadDonors()) for clearing input fields and reloading donor information, respectively.

**Exception Handling:**

*Exceptions are caught and printed to the console for debugging purposes. In a production environment, you might want to handle exceptions more gracefully, perhaps by displaying error messages to the user.

Overall, this code provides a basic foundation for a Blood Donation Management System with a simple GUI and database integration

---
