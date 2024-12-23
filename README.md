# CQQCH - Cariño, ¿Qué Quieres Comer Hoy? 🍽️

CQQCH es una aplicación desarrollada en **Android Studio** con el lenguaje **Java**, diseñada para ayudar a los usuarios a gestionar sus opciones culinarias, incluyendo restaurantes y recetas favoritas. La aplicación permite tomar decisiones rápidas y centralizar toda la información relacionada con la comida en un solo lugar. La aplicación utiliza **Firebase** como backend, aprovechando tanto **Authentication** para gestionar usuarios como **Realtime Database** para almacenar y sincronizar los datos.

---

## 🚀 **Características Principales**

### 🏢 **1. Gestión de Restaurantes**
Los usuarios pueden agregar restaurantes con los siguientes detalles:

- Nombre 🏷️
- Dirección 📍
- Nota (puntuación del 1 al 5) ⭐
- Categoría 🍴 (Italianos, Asiáticos, Regionales, etc.)
- ¿Se puede ir? ✅ (Sí o No)
- ¿Se puede pedir? 🛵 (Sí o No)
- Precio aproximado 💰
- Comentarios adicionales 📝

Cada restaurante se asocia al usuario que lo crea y se almacena en **Firebase**, garantizando privacidad y sincronización en diferentes dispositivos. Otros usuarios no pueden acceder ni editar los datos del restaurante.

### 📋 **2. Visualización de Restaurantes**
Los usuarios pueden acceder a una lista de restaurantes añadidos con los detalles completos, incluyendo:

- Categoría seleccionada.
- Indicaciones sobre "¿Se puede ir?" y "¿Se puede pedir?" según las opciones elegidas.
- Iconos intuitivos para distinguir favoritos y opciones de eliminación.

---

### 🍳 **3. Gestión de Recetas**
Los usuarios pueden agregar sus recetas personales con los siguientes detalles:

- Nombre de la receta 🏷️
- Categoría 🍽️ (Carne, Pescado, Postres, etc.)
- Preparación ✍️ (indicaciones detalladas)
- Nota (puntuación del 1 al 5) ⭐
- Precio estimado 💰
- Tiempo de preparación ⏱️

Cada receta se asocia al usuario que la crea, almacenándose en **Firebase** con la misma garantía de privacidad y sincronización.

### 📋 **4. Visualización de Recetas**
Las recetas añadidas por el usuario se muestran en una lista con todos los detalles visibles, permitiendo una navegación fluida y clara.

---

### 🌟 **Funcionalidades Implementadas**

#### ❤️ **1. Sistema de Favoritos**
- **Descripción:** Los usuarios pueden marcar restaurantes o recetas como favoritos mediante un icono de corazón.
  - Corazón relleno (♥) para favoritos.
  - Corazón vacío (♡) para no favoritos.
- **Funcionamiento:** Al marcar un restaurante o receta como favorito, la información se almacena automáticamente en **Firebase**.
- **Visualización:** Existe un apartado exclusivo para mostrar solo favoritos.

#### 🎲 **2. Funcionalidad "Cariño, elige tú"**
- **Descripción:** Herramienta para usuarios indecisos que permite filtrar y seleccionar un restaurante o receta según preferencias.
- **Filtros disponibles:**
  - Restaurantes o recetas.
  - Categorías específicas.
  - Favoritos (Sí o No).
  - Nota (1 a 5).
  - Rango de precios (de 10 en 10, hasta 100).

#### 🗑️ **3. Implementación: Borrar Restaurante o Receta**
- **Descripción:** Los usuarios pueden eliminar un restaurante o receta de su lista.
- **Funcionamiento:** Cada elemento tiene un icono de borrado que, al pulsar, elimina el registro de la base de datos.
- **Sincronización:** Las listas se actualizan automáticamente para reflejar los cambios.

---

## 🛠️ **Tecnologías Utilizadas**

- **Lenguaje de programación:** Java.
- **IDE:** Android Studio.
- **Base de datos:** Firebase Realtime Database.
- **Autenticación:** Firebase Authentication.
- **Diseño de UI:** XML con soporte para RecyclerView y ConstraintLayout.
- **Modo Oscuro:** Soporte completo en todas las actividades principales.

---

## ✨ **Implementación de Nuevas Funcionalidades**

- **Mejoras en el Menú Lateral:**
  - Creación de `PoliticaPrivacidadActivity` y `AcercaDeActivity`.
  - Opciones para Política de Privacidad y Acerca de.
- **Diseño Inicial de `AjustesActivity`:**
  - Opción para cambiar contraseña de usuario.
  - Posibilidad de eliminar la cuenta, reflejando los cambios en **Firebase**.
- **Optimización del Modo Oscuro:**
  - Ajustes de colores para Menú Lateral, Política de Privacidad y Acerca de.
- **Mejoras en la UI:**
  - Versión horizontal para elegir fotos.
  - Rediseño del menú inferior para mejorar la experiencia de usuario.
- **Correción en EligeTú:**
  - Filtros claros y jerárquicos.
  - Presentación de resultados en un RecyclerView.

---

## 🔒 **Acceso y Gestión de Datos en Firebase**
- **Firebase Authentication:** Gestiona usuarios de forma segura, permitiendo registro, inicio de sesión y recuperación de cuentas.
- **Firebase Realtime Database:**
  - Datos organizados por nodos (`Restaurantes`, `Recetas`, `Usuarios`).
  - **Visualización como administrador:** Los datos pueden ser accedidos y gestionados desde la consola de Firebase para tareas de mantenimiento.

---

CQQCH es más que una aplicación culinaria: es una herramienta para simplificar decisiones, almacenar recuerdos culinarios y explorar nuevas experiencias gastronómicas.
