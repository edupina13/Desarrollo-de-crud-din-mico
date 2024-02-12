<template>
  <div style="padding-top: 30px; padding-left: 20px ; ">
    <b-button pill size="lg" v-b-modal.modal-1 style="background-color: #2169d6; color:" >Agregar Libro</b-button>

    <b-modal id="modal-1" title="Agregar Libro"  hide-footer hide-header-close centered >
    <b-form @submit.prevent="onSubmit">
      <b-form-group label="Nombre:" label-for="input-1" label-cols-sm="3" label-align-sm="right" label-size="sm">
        <b-col sm="9">
          <b-form-input id="input-1" v-model="form.nombre" placeholder="Titulo" required></b-form-input>
        </b-col>
      </b-form-group>

      <b-form-group label="Autor:" label-for="input-2" label-cols-sm="3" label-align-sm="right" label-size="sm">
        <b-col sm="9">
          <b-form-input id="input-2" v-model="form.autor" placeholder="Autor" required></b-form-input>
        </b-col>
      </b-form-group>

      <b-form-group label="Género:" label-for="input-3" label-cols-sm="3" label-align-sm="right" label-size="sm">
        <b-col sm="9">
          <b-form-select v-model="form.genero" :options="options"></b-form-select>
        </b-col>
      </b-form-group>

      <b-form-group label="Año de Publicación:" label-for="input-4" label-cols-sm="3" label-align-sm="right" label-size="sm">
        <b-col sm="9">
          <b-form-input id="input-4" v-model="form.anioPublicacion" type="number" placeholder="1880" required></b-form-input>
        </b-col>
      </b-form-group>

      <b-row class="mt-3">
    <b-col sm="12" class="text-right">
        <b-button type="submit" variant="primary" style="margin-right: 3px;">Enviar</b-button>
        <b-button variant="secondary" @click="$bvModal.hide('modal-1')" style="margin-left: 3px;">Cancelar</b-button>
    </b-col>
</b-row>
   
    </b-form>
  </b-modal>
  
    <div class="d-flex justify-content-center align-items-center" >
    <b-spinner v-if="showSpinner" variant="info" style="width: 8rem; height: 8rem; margin-top:15%;"></b-spinner>

    
  </div>

  <b-card-group deck class="getlibros">
    <b-card v-for="(libro, index) in libros" :key="index" class="custom-card">
      <template #header >
        <h5 class="card-title" >{{ libro.nombre }}</h5>
      </template>
      <b-card-text>
        <p><strong>Autor:</strong> {{ libro.autor }}</p>
        <p><strong>Género:</strong> {{ libro.genero }}</p>
        <p><strong>Año de Publicación:</strong> {{ libro.anioPublicacion }}</p>
      </b-card-text>
    </b-card>
  </b-card-group>
  </div>
  
</template>

<script>
export default {
  data() {
    return {
      options: [
        { value: null, text: "SELECCIONA UN GÉNERO" },
        { value: "terror", text: "Terror" },
        { value: "novela", text: "Novela" },
        { value: "accion", text: "Accion"},
        { value: "cuento", text: "Cuento"},
        { value: "poesía", text: "Poesía"},
        { value: "humor", text: "Humor"},
        { value: "ficción", text: "Ficcíon"},
        { value: "libro de autoayuda", text: "Libro de autoayuda"},


      ],
      form: {},
      libros: [],
      showSpinner: false,
    };
  },
  mounted() {
    this.spinner(); 
  },
  methods: {
    onSubmit() {
      console.log("Successfully send!");
      this.$bvModal.hide("modal-1");

      var url = "http://localhost:8080/api/libros";
      var data = this.form;

      fetch(url, {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
          "Content-Type": "application/json",
        },
      })
      .then((res) => res.json())
      .then((response) => {
        console.log("Success:", response);
        location.reload();
      })
      .catch((error) => console.error("Error:", error));
    },

    spinner() {
      this.showSpinner = true;
      setTimeout(() => {
        this.showSpinner = false;
        this.getLibros();
      }, 1500);
    },

    getLibros() {
      fetch("http://localhost:8080/api/libros")
        .then((response) => {
          if (!response.ok) {
            throw new Error("");
          }
          return response.json();
        })
        .then((data) => {
          console.log("Success:", data);
          this.libros = data;
        })
        .catch((error) => {
          console.error("Error:", error);
        });
    }
  },
};
</script>

<style scoped>
.getlibros {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-gap: 20px; 
  margin-top: 20px;
}

.custom-card {
  padding: 20px;
  margin-bottom: 20px;
  background-color: #c2edf7; 
  border: 1px solid #dee2e6;
  border-radius: 10px; 
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.card-title {
 
  font-size: 1.5rem;
  font-weight: bold;
  color: #1577e0;
}
</style>
