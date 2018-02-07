import { IModel, ModelContainer } from "./models"

export abstract class Recognizer {
  private readonly modelContainer: ModelContainer = new ModelContainer();

  getModel(modelTypeName: string, culture: string): IModel {
    return this.modelContainer.getModel(modelTypeName, culture);
  }

  tryGetModel(modelTypeName: string, culture: string): { containsModel: boolean; model?: IModel } {
    return this.modelContainer.tryGetModel(modelTypeName, culture);
  }

  containsModel(modelTypeName: string, culture: string): boolean {
    return this.modelContainer.containsModel(modelTypeName, culture);
  }

  registerModel(modelTypeName: string, culture: string, model: IModel) {
    this.modelContainer.registerModel(modelTypeName, culture, model);
  }

  registerModels(models: Map<string, IModel>, culture: string) {
    this.modelContainer.registerModels(models, culture);
  }
}