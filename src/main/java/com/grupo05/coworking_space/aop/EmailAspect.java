package com.grupo05.coworking_space.aop;

import com.grupo05.coworking_space.dto.RequestReservationDTO;
import com.grupo05.coworking_space.dto.ReservationDTO;
import com.grupo05.coworking_space.dto.RoomDTO;
import com.grupo05.coworking_space.dto.UserDTO;
import com.grupo05.coworking_space.service.EmailSender;
import com.grupo05.coworking_space.service.RoomService;
import com.grupo05.coworking_space.service.UserDetailsServiceImpl;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Aspecto para el envío automático de correos electrónicos después de la creación de una reserva.
 */
@Aspect
@Component
public class EmailAspect {

    private final EmailSender emailSender;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private RoomService roomService;

    public EmailAspect(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    /**
     * Método que se ejecuta después de la creación de una reserva para enviar notificaciones por correo electrónico
     * a los participantes de la reunión.
     *
     * @param requestReservationDTO DTO con los datos de la reserva y los participantes.
     */
    @AfterReturning(value = "execution(* com.grupo05.coworking_space.service.ReservationService.createReservation(..)) && args(requestReservationDTO)")
    public void sendNotification(RequestReservationDTO requestReservationDTO) {
        ReservationDTO reservationDTO = requestReservationDTO.getReservationDTO();
        UserDTO userDTO = userDetailsService.findUserById(reservationDTO.getUserFK());
        RoomDTO roomDTO=roomService.findRoomById(reservationDTO.getRoomsFK().getFirst());
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime dateTime = reservationDTO.getDateInit();
        String dateFormat = format.format(dateTime);
        String hora = String.format("%02d:%02d", dateTime.getHour(), dateTime.getMinute());
        String body = userDTO.getUsername() +
                " te invitó a participar de la reunión para el " + dateFormat +
                " a las " + hora +"hs en " +
                roomDTO.getName();
        List<String> emailsParticipants = requestReservationDTO.getEmailsParticipants();
        if(emailsParticipants!=null && !emailsParticipants.isEmpty()) {
            for(String email : emailsParticipants){
                emailSender.sendEmail(email, "Reserva", body);
            }
        }

    }
}
