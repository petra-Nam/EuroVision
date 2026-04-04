// package com.example;

// import com.example.controller.RegistrationController;
// import com.example.model.Country;
// import com.example.repository.VoterRepository;
// import com.example.repository.CountryRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.when;

// public class AppTest {

//     private RegistrationController registrationController;
//     private VoterRepository voterRepository;
//     private CountryRepository countryRepository;

//     @BeforeEach
//     public void setup() {
//         voterRepository = Mockito.mock(VoterRepository.class);
//         countryRepository = Mockito.mock(CountryRepository.class);
//         registrationController = new RegistrationController(voterRepository, countryRepository);
//     }

//     @Test
//     public void testRegisterVoter() {
//         // Mock the CountryRepository to return a Country object
//         Country mockCountry = new Country();
//         mockCountry.setName("Austria");
//         when(countryRepository.findByName("Austria")).thenReturn(java.util.Optional.of(mockCountry));

//         // Call the registerVoter method
//         String username = "testUser";
//         String password = "testPass";
//         String countryName = "Austria";
//         boolean isJury = false;

//         String result = registrationController.registerVoter(username, password, countryName, isJury);

//         // Verify the result
//         assertEquals("Voter registered successfully!", result);
//     }
// }