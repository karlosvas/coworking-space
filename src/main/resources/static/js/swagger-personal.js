// Personalización de Swagger UI
document.addEventListener('DOMContentLoaded', function () {
  // Primero cargamos la librería SweetAlert2 dinámicamente
  let sweetAlertScript = document.createElement('script');
  sweetAlertScript.src = 'https://cdn.jsdelivr.net/npm/sweetalert2@11';
  sweetAlertScript.onload = function () {
    console.log('SweetAlert cargado correctamente');
    iniciarDeteccionSwaggerUI();
  };
  document.head.appendChild(sweetAlertScript);

  // También añadimos los estilos de SweetAlert
  let sweetAlertStyles = document.createElement('link');
  sweetAlertStyles.rel = 'stylesheet';
  sweetAlertStyles.href = 'https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css';
  document.head.appendChild(sweetAlertStyles);
});

// Variable global para controlar si las notificaciones están activas
let notificacionesActivas = true;
function agregarToggleNotificaciones() {
  // Crear un toggle button para activar/desactivar notificaciones
  const toggleContainer = document.createElement('div');
  toggleContainer.style.position = 'fixed';
  toggleContainer.style.top = '70px';
  toggleContainer.style.right = '10px';
  toggleContainer.style.zIndex = '9999';
  toggleContainer.style.display = 'flex';
  toggleContainer.style.alignItems = 'center';
  toggleContainer.style.backgroundColor = 'rgba(0, 0, 0, 0.7)';
  toggleContainer.style.padding = '5px 10px';
  toggleContainer.style.borderRadius = '4px';

  // Agregar etiqueta de texto
  const label = document.createElement('span');
  label.textContent = 'Notificaciones: ';
  label.style.marginRight = '5px';
  label.style.color = '#fff';
  toggleContainer.appendChild(label);

  // Crear el botón toggle
  const toggleBtn = document.createElement('button');
  toggleBtn.id = 'toggle-notifications';
  toggleBtn.style.padding = '5px 10px';
  toggleBtn.style.borderRadius = '4px';
  toggleBtn.style.border = 'none';
  toggleBtn.style.cursor = 'pointer';
  toggleBtn.style.backgroundColor = notificacionesActivas ? '#4caf50' : '#f44336';
  toggleBtn.style.color = '#fff';
  toggleBtn.style.fontWeight = 'bold';
  toggleBtn.textContent = notificacionesActivas ? 'ON' : 'OFF';
  toggleBtn.title = 'Activar/Desactivar notificaciones';

  // Manejar clics en el botón
  toggleBtn.addEventListener('click', function () {
    notificacionesActivas = !notificacionesActivas;
    toggleBtn.textContent = notificacionesActivas ? 'ON' : 'OFF';
    toggleBtn.style.backgroundColor = notificacionesActivas ? '#4caf50' : '#f44336';

    // Guardar preferencia en localStorage
    localStorage.setItem('swaggerNotificaciones', notificacionesActivas);

    // Mostrar mensaje de confirmación
    const mensaje = notificacionesActivas ?
      'Notificaciones activadas' :
      'Notificaciones desactivadas';

    if (notificacionesActivas) {
      Swal.fire({
        title: 'Configuración actualizada',
        text: mensaje,
        icon: 'info',
        timer: 2000,
        timerProgressBar: true,
        showConfirmButton: false
      });
    } else {
      console.log(mensaje);
    }
  });

  toggleContainer.appendChild(toggleBtn);

  // Añadir al body
  document.body.appendChild(toggleContainer);

  // Recuperar preferencia guardada
  const savedPreference = localStorage.getItem('swaggerNotificaciones');
  if (savedPreference !== null) {
    notificacionesActivas = savedPreference === 'true';
    toggleBtn.textContent = notificacionesActivas ? 'ON' : 'OFF';
    toggleBtn.style.backgroundColor = notificacionesActivas ? '#4caf50' : '#f44336';
  }
}

function iniciarDeteccionSwaggerUI() {
  // Verificar múltiples posibles puntos de detección para Swagger UI
  const intervalId = setInterval(() => {
    // Métodos alternativos para detectar Swagger UI
    if (document.querySelector('.swagger-ui')) {
      clearInterval(intervalId);
      // Swagger UI detectado
      configurarInterceptorSwagger();
      agregarToggleNotificaciones();
    } else {
      console.error('Esperando Swagger UI...');
    }
  }, 1000); // Revisar cada segundo

  // Mayor tiempo de espera antes de detener la búsqueda
  setTimeout(() => {
    if (intervalId) {
      clearInterval(intervalId);
      configurarInterceptorSwagger();
      agregarToggleNotificaciones();
    }
  }, 30000); // 30 segundos de espera
}

function configurarInterceptorSwagger() {
  // Interceptar las peticiones de Swagger UI
  const originalFetch = window.fetch;

  window.fetch = async function (input, init) {
    let requestUrl = '';

    // Determinar la URL de la petición
    requestUrl = input;

    // Solo monitorear peticiones a la API
    if (requestUrl.includes('/api/') && notificacionesActivas) {
      try {
        const response = await originalFetch(input, init);
        console.log(`Respuesta recibida para ${requestUrl}:`, response.status);

        if (response.status === 204) {
          console.log('Respuesta 204 No Content - Operación exitosa sin contenido');
          Swal.fire({
            title: 'Éxito 204',
            text: 'Operación completada correctamente, pero no hay contenido para mostrar',
            icon: 'success',
            confirmButtonText: 'Aceptar'
          });
          return response;
        }

        const clonedResponse = response.clone();

        // Verificar el tipo de contenido para determinar si es JSON
        if (response.status === 401 || response.status === 403) {
          text = 'No autorizado';
          Swal.fire({
            title: `Error ${response.status}`,
            text: text || 'Respuesta no válida o no autorizada',
            icon: 'error',
            confirmButtonText: 'Aceptar'
          });
          return response;
        }

        try {
          // Intentar leer la respuesta JSON para mostrar mensajes personalizados
          const data = await clonedResponse.json();
          console.log('Datos recibidos:', data);
          let status = data.statusCode || data.code;
          if (!data.hasError && status >= 200 && status < 300) {
            Swal.fire({
              title: `Éxito ${status}`,
              text: data.message,
              icon: 'success',
              confirmButtonText: 'Aceptar'
            });
          } else {
            Swal.fire({
              title: `Error ${status}`,
              text: data.message,
              icon: 'error',
              confirmButtonText: 'Aceptar'
            });
          }
        } catch (jsonError) {
          console.log(data);
          Swal.fire({
            title: `Error ${response.status}`,
            text: 'Se a producido un error en la comunicación con el servidor',
            icon: 'error',
            confirmButtonText: 'Aceptar'
          });
        }
        return response;
      } catch (networkError) {
        // Error de red (sin conexión, timeout, etc.)
        Swal.fire({
          title: `Error`,
          text: 'No se pudo establecer conexión con el servidor',
          icon: 'error',
          confirmButtonText: 'Aceptar'
        });
        return response;
      }
    } else {
      return originalFetch(input, init);
    }
  };
}