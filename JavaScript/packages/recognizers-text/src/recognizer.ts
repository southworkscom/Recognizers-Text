import { IModel, ModelContainer } from "./models"

export abstract class Recognizer {
  private readonly modelContainer: ModelContainer = new ModelContainer();

  getModel(modelTypeName: string, culture: string): IModel {
    return this.modelContainer.getModel(modelTypeName, culture);
  }

  registerModel(modelTypeName: string, culture: string, model: IModel) {
    this.modelContainer.registerModel(modelTypeName, culture, model);
  }
}