🚨 Disaster Alerts & Evacuation Route Planner 🗺️

An intelligent, real-time disaster management system designed to minimize response time and loss of life during emergencies such as floods, earthquakes, and industrial accidents.

This project is submitted in partial fulfillment of the requirements for the degree of Bachelor of Engineering at the University of Mumbai.

📌 Project Overview

The Disaster Alerts and Evacuation Route Planner is an automated, real-time disaster response platform focused on speed, accuracy, and reliability.

The system:

Instantly broadcasts disaster alerts

Automatically assigns the nearest safe shelter

Computes the shortest evacuation route

Helps individuals assess their safety status based on location

The core goal is to reduce human intervention, response delay, and confusion during emergencies.

🎯 Core Objectives

Provide instant disaster alerts to affected regions

Automatically allocate the nearest available shelter

Offer clear and optimized evacuation routes

Enable users to check safety status in real time

Allow administrators to manage disasters and shelters efficiently

✨ Key Features
🔔 Real-Time Disaster Alerting

Admins can create disaster alerts with:

Geographic coordinates

Severity level

Alerts are broadcast in real time using Server-Sent Events (SSE) for low-latency updates.

🏠 Automated Shelter Allocation

Intelligent allocation engine selects the nearest non-full shelter

Uses geospatial distance calculations based on the Haversine Formula

Ensures:

Fast computation

Capacity-aware shelter assignment

🧭 Shortest Evacuation Route Planner

Computes the shortest evacuation path from disaster location to assigned shelter

Conceptually aligned with algorithms like Dijkstra’s / A* for optimal routing

Provides clear movement guidance for affected users

🧑‍🚒 User Safety Status Check

Public-facing module for users

Determines whether the user is within:

A 5 km disaster impact radius

Displays:

Safety status

Nearest shelter details

Evacuation guidance (if applicable)

🖥️ Admin Dashboard

Centralized interface for administrators

Features include:

Disaster alert creation & management

Shelter CRUD operations

Capacity monitoring

Real-time allocation visualization

💻 Technology Stack
Component	Technology	Description
Backend Framework	Spring Boot 3.x, Java 17	Modular, scalable, and production-grade backend
Real-Time Updates	Server-Sent Events (SSE)	Low-latency, real-time communication
Database	PostgreSQL	Reliable relational data storage
Geospatial Logic	Haversine Formula	Accurate great-circle distance calculation
Development Methodology	Agile	Iterative development with continuous improvements
⚙️ Algorithms Used
1️⃣ Haversine Distance Calculation

Used to compute the distance between a disaster location and shelters on the Earth’s surface.

Formula:

d = 2R · arcsin(
  √(
    sin²(Δφ / 2) +
    cos(φ₁) · cos(φ₂) · sin²(Δλ / 2)
  )
)


Where:

R = Earth’s radius

φ = latitude (in radians)

Δφ, Δλ = differences in latitude and longitude

Time Complexity: O(1)

2️⃣ Nearest Shelter Selection Algorithm

Iterates through all available shelters

Selects the closest shelter that is not at full capacity

Time Complexity: O(n)

3️⃣ Shortest Route Computation

Conceptually similar to Dijkstra’s / A* algorithms

Determines the optimal evacuation path on the road network

Ensures minimum travel distance and clear navigation

🚀 Future Scope

Planned enhancements for upcoming iterations include:

Advanced Route Optimization

Integration with APIs like:

Google Directions API

OSRM

Real-time traffic-aware routing

IoT Sensor Integration

Automated disaster detection using:

Flood sensors

Seismic sensors

Fire detection systems

Mobile Application

Native Android & iOS applications

Improved accessibility and navigation support

AI-Based Severity Prediction

Predict disaster intensity using AI

Dynamically adjust:

Alert radius

Shelter allocation

Routing constraints

📚 Academic Context

This project demonstrates practical application of:

Geospatial algorithms

Real-time system design

Backend engineering using Spring Boot

Disaster management automation

It reflects a real-world, scalable engineering solution aligned with modern software development practices.

📄 License

This project is developed for academic and educational purposes.
Further usage or deployment may require additional permissions.
