Veterinary Management System
The Veterinary Management System is a comprehensive REST API designed to streamline and manage the daily operations of veterinary clinics. This API enables veterinary staff to efficiently handle veterinarians, customers, animals, vaccines, and appointments.

API Endpoints
The system includes various endpoints categorized by entities such as customers, animals, vaccines, doctors, available dates, and appointments. Each category allows for typical CRUD (Create, Read, Update, Delete) operations as well as specific queries to facilitate efficient data management.

Customers
Retrieve a customer by ID: GET /api/v1/customers/{id}
Update a customer by ID: PUT /api/v1/customers/{id}
Delete a customer by ID: DELETE /api/v1/customers/{id}
Retrieve all customers: GET /api/v1/customers
Add a new customer: POST /api/v1/customers
Search customers by name: GET /api/v1/customers/searchByName
Animals
Retrieve an animal by ID: GET /api/v1/animals/{id}
Update an animal by ID: PUT /api/v1/animals/{id}
Delete an animal by ID: DELETE /api/v1/animals/{id}
Retrieve all animals: GET /api/v1/animals
Add a new animal: POST /api/v1/animals
Search animals by name: GET /api/v1/animals/searchByName
Filter animals by owner: GET /api/v1/animals/searchByCustomer
Vaccines
Retrieve a vaccine by ID: GET /api/v1/vaccines/{id}
Update a vaccine by ID: PUT /api/v1/vaccines/{id}
Delete a vaccine by ID: DELETE /api/v1/vaccines/{id}
Retrieve all vaccines: GET /api/v1/vaccines
Add a new vaccine: POST /api/v1/vaccines
Retrieve vaccination records by date range: GET /api/v1/vaccines/searchByVaccinationRange
Retrieve all vaccination records for an animal: GET /api/v1/vaccines/searchByAnimal
Doctors
Retrieve a doctor by ID: GET /api/v1/doctors/{id}
Update a doctor by ID: PUT /api/v1/doctors/{id}
Delete a doctor by ID: DELETE /api/v1/doctors/{id}
Retrieve all doctors: GET /api/v1/doctors
Add a new doctor: POST /api/v1/doctors
Available Dates
Retrieve an available date by ID: GET /api/v1/available_dates/{id}
Update an available date by ID: PUT /api/v1/available_dates/{id}
Delete an available date by ID: DELETE /api/v1/available_dates/{id}
Retrieve all available dates: GET /api/v1/available_dates
Add a new available date: POST /api/v1/available_dates
Appointments
Retrieve an appointment by ID: GET /api/v1/appointments/{id}
Update an appointment by ID: PUT /api/v1/appointments/{id}
Delete an appointment by ID: DELETE /api/v1/appointments/{id}
Retrieve all appointments: GET /api/v1/appointments
Add a new appointment: POST /api/v1/appointments
Filter appointments by date range and doctor: GET /api/v1/appointments/searchByDoctorAndDateRange
Filter appointments by date range and animal: GET /api/v1/appointments/searchByAnimalAndDateRange
