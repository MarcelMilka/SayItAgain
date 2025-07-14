# :connectivity

---



## Overview 
Provides logic that tracks and exposes the runtime connectivity status.

---



## Details
###  `ConnectivityManagerImpl`

A concrete implementation of the `ConnectivityObserver` interface, responsible for observing the device’s
network connectivity status in real time using Android’s `ConnectivityManager`

- Maintains an observable state using a `MutableStateFlow<ConnectivityStatus>` which emits updates to subscribers when
connectivity changes occur.
- Emits one of two possible states: `Connected` or `Disconnected`.
- Registers a NetworkCallback to monitor internet-capable networks, specifically over Wi-Fi and cellular transports.

###  `ConnectivityManagerImpl`

A Dagger-Hilt module that provides a singleton instance of ConnectivityObserver.

---